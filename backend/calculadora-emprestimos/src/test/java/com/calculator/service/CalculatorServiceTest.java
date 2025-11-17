package com.calculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;

import com.calculator.dto.LoanInstallmentDto;
import com.calculator.dto.LoanRequestDto;
import com.calculator.exception.DateException;

class CalculatorServiceTest {

    private CalculatorService service;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    @BeforeEach
    void setUp() {
        service = new CalculatorService();
    }

    private LoanRequestDto createRequest(LocalDate start, LocalDate finalDate, LocalDate firstPayment, BigDecimal amount, BigDecimal rate) {
        return new LoanRequestDto(start, finalDate, firstPayment, amount, rate);
    }

    @Test
    @DisplayName("Deve identificar um feriado fixo corretamente (Natal)")
    void isHoliday_FixedHoliday() throws Exception {
        LocalDate natal = LocalDate.of(2025, 12, 25);
    
        assertTrue(service.isHoliday(natal), "O Natal deve ser reconhecido como feriado.");
    }
    
    @Test
    @DisplayName("Deve ajustar um pagamento que cai em feriado ou fim de semana para o próximo dia útil")
    void getNextBusinessDay_AdjustsCorrectly() throws Exception {
        LocalDate dataProblematica = LocalDate.of(2025, 11, 15); 
        LocalDate proximoDiaUtilEsperado = LocalDate.of(2025, 11, 17);
    
        assertEquals(proximoDiaUtilEsperado, service.getNextBusinessDay(dataProblematica), 
                 "A data de pagamento deve ser ajustada para a próxima segunda-feira.");
    }

    @Test
    @DisplayName("Deve lançar DateException se data final for antes da inicial")
    void validateBusinessRules_FinalDateBeforeStart() {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate end = LocalDate.of(2024, 12, 31);
        LocalDate firstPayment = LocalDate.of(2025, 2, 1);
        LoanRequestDto request = createRequest(start, end, firstPayment, BigDecimal.TEN, BigDecimal.ZERO);

        Exception exception = assertThrows(DateException.class, () -> service.validateBusinessRules(request));
        assertEquals("Erro: A data final deve ser maior que a data inicial", exception.getMessage());
    }
    
    @ParameterizedTest
    @CsvSource({
        "10000.00, 0.015, 3"
    })
    @DisplayName("Deve calcular a tabela SAC e incluir todas as linhas de provisão (Fim de Mês) e pagamento.")
    void calculate_SacLoanWithProvisionLines(BigDecimal amount, BigDecimal rate, int months) {
        LocalDate start = LocalDate.of(2025, 1, 1);
        LocalDate firstPayment = LocalDate.of(2025, 2, 1);
        LocalDate finalDate = LocalDate.of(2025, 4, 1); 
        
        LoanRequestDto request = createRequest(start, finalDate, firstPayment, amount, rate);
        List<LoanInstallmentDto> result = service.calculate(request);

        final int expectedEvents = 7; 
        
        assertEquals(expectedEvents, result.size(), 
                     "O número total de linhas deve ser 7 (Linha 0 + 3 Provisões + 3 Pagamentos).");

        LoanInstallmentDto linhaZero = result.get(0);
        assertEquals(amount.setScale(2, ROUNDING_MODE), linhaZero.principalBalance().setScale(2, ROUNDING_MODE), "Saldo Capital na Linha 0 incorreto.");
        
        LoanInstallmentDto pProvision1 = result.get(1); 
        assertEquals(LocalDate.of(2025, 1, 31), pProvision1.effectiveDate(), "Linha 1 deve ser a provisão de 31/01.");
        assertEquals(BigDecimal.ZERO.setScale(2, ROUNDING_MODE), pProvision1.totalPayment(), "Provisão não deve ter pagamento total.");
        assertTrue(pProvision1.provision().doubleValue() > 0, "Provisão de juros deve ser maior que zero.");
        
        BigDecimal expectedAmortization = amount.divide(new BigDecimal(months), 2, ROUNDING_MODE);
        LoanInstallmentDto p1 = result.get(2); 

        assertEquals(LocalDate.of(2025, 2, 3), p1.effectiveDate(), "Data Competência (P1) deve ser ajustada para 03/02.");
        assertEquals(expectedAmortization, p1.amortization().setScale(2, ROUNDING_MODE), "Amortização da P1 incorreta.");
        assertEquals(new BigDecimal("6666.67"), p1.principalBalance().setScale(2, ROUNDING_MODE), "Saldo Capital após P1 incorreto."); 
        assertEquals("1/3", p1.consolidated(), "Consolidada (Label) incorreta.");
        
        LoanInstallmentDto p2 = result.get(4);
        assertEquals(expectedAmortization, p2.amortization().setScale(2, ROUNDING_MODE), "Amortização da P2 incorreta.");
        
        assertEquals(new BigDecimal("3333.33"), p2.principalBalance().setScale(2, ROUNDING_MODE), "Saldo Capital após P2 incorreto."); 
        
        assertEquals("2/3", p2.consolidated(), "Consolidada (Label) incorreta.");

        LoanInstallmentDto p3 = result.get(6);
        
        assertEquals(new BigDecimal("3333.33"), p3.amortization().setScale(2, ROUNDING_MODE), "Amortização da P3 incorreta (inclui ajuste de 0.00).");
        
        assertEquals(BigDecimal.ZERO.setScale(2, ROUNDING_MODE), p3.principalBalance().setScale(2, ROUNDING_MODE), "Saldo Capital Final deve ser zero.");
        assertEquals("3/3", p3.consolidated(), "Consolidada (Label) incorreta.");
    }
}