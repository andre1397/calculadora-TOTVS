package com.calculator.service;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.time.temporal.TemporalAdjusters;
import java.util.ArrayList;
import java.util.List;
import java.util.SortedSet;
import java.util.TreeSet;

import org.springframework.stereotype.Service;

import com.calculator.dto.LoanInstallmentDto;
import com.calculator.dto.LoanRequestDto;
import com.calculator.exception.DateException;
import com.calculator.exception.NotFoundInstallments;

@Service
public class CalculatorService {
    private static final int DAY_BASE = 360;
    private static final int CALCULATION_SCALE = 30;
    private static final int CURRENCY_SCALE = 2;
    private static final RoundingMode ROUNDING_MODE = RoundingMode.HALF_UP;

    /**
     * Calcula e retorna uma lista com as parcelas de um empréstimo
     * @param request
     * @return
     */
    public List<LoanInstallmentDto> calculate(LoanRequestDto request) {
        validateBusinessRules(request);  

        LocalDate startDate = request.startDate();
        LocalDate finalDate = request.finalDate();
        LocalDate firstPaymentDate = request.firstPaymentDate();
        BigDecimal loanAmount = request.loanAmount();

        SortedSet<LocalDate> eventDates = generateEventDates(
                startDate,
                finalDate,
                firstPaymentDate
        );

        List<LocalDate> paymentDates = new ArrayList<>(eventDates.stream()
                .filter(paymentDate -> paymentDate.isAfter(startDate) && !paymentDate.equals(paymentDate.with(TemporalAdjusters.lastDayOfMonth())) || paymentDate.equals(finalDate))
                .filter(paymentDate -> paymentDate.isEqual(firstPaymentDate) || paymentDate.isAfter(firstPaymentDate))
                .toList());

        if (!paymentDates.contains(finalDate)) {
             paymentDates.add(finalDate);
        }

        int numberOfInstallments = paymentDates.size();
        if (numberOfInstallments == 0) {
            throw new NotFoundInstallments("Nenhuma parcela de pagamento encontrada no período.");
        }

        BigDecimal constantAmortization = loanAmount
                .divide(new BigDecimal(numberOfInstallments), CALCULATION_SCALE, ROUNDING_MODE);

        List<LoanInstallmentDto> InstallmentList = new ArrayList<>(); 
        BigDecimal principalBalance = loanAmount; 
        BigDecimal accumulatedInterest = BigDecimal.ZERO;  
        LocalDate previousDate = startDate; 
        int currentInstallment = 0; 

        InstallmentList.add(new LoanInstallmentDto(
                startDate,
                loanAmount,
                loanAmount,
                null,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                principalBalance,
                BigDecimal.ZERO,
                BigDecimal.ZERO,
                BigDecimal.ZERO
        ));

        for (LocalDate currentDate : eventDates) {
            if (currentDate.isEqual(startDate)) {
                continue;
            }
            
            long elapsedDays = ChronoUnit.DAYS.between(previousDate, currentDate);
            if (elapsedDays <= 0) {
                continue;
            }

            BigDecimal previousOutstandingBalance = principalBalance.add(accumulatedInterest);

            double rateAsDouble = request.interestRate().doubleValue();
            double interestFactor = Math.pow(1.0 + rateAsDouble, (double) elapsedDays / DAY_BASE) - 1.0;
            BigDecimal periodInterest = previousOutstandingBalance
                    .multiply(new BigDecimal(interestFactor))
                    .setScale(CALCULATION_SCALE, ROUNDING_MODE);

            accumulatedInterest = accumulatedInterest.add(periodInterest);

            BigDecimal amortizationPaid = BigDecimal.ZERO;
            BigDecimal interestPaid = BigDecimal.ZERO;
            BigDecimal totalInstallmentPayment = BigDecimal.ZERO;
            String consolidatedLabel = null;

            if (paymentDates.contains(currentDate)) {
                currentInstallment++;
                amortizationPaid = constantAmortization;
                interestPaid = accumulatedInterest;
                totalInstallmentPayment = amortizationPaid.add(interestPaid);
                
                principalBalance = principalBalance.subtract(amortizationPaid);
                accumulatedInterest = BigDecimal.ZERO;

                consolidatedLabel = currentInstallment + "/" + numberOfInstallments;

                if (currentInstallment == numberOfInstallments) {
                    amortizationPaid = amortizationPaid.add(principalBalance);
                    principalBalance = BigDecimal.ZERO;
                }
            }

            BigDecimal currentOutstandingBalance = principalBalance.add(accumulatedInterest);

            InstallmentList.add(new LoanInstallmentDto(
                    currentDate,
                    BigDecimal.ZERO,
                    currentOutstandingBalance.setScale(CURRENCY_SCALE, ROUNDING_MODE),
                    consolidatedLabel,
                    totalInstallmentPayment.setScale(CURRENCY_SCALE, ROUNDING_MODE),
                    amortizationPaid.setScale(CURRENCY_SCALE, ROUNDING_MODE),
                    principalBalance.setScale(CURRENCY_SCALE, ROUNDING_MODE),
                    periodInterest.setScale(CURRENCY_SCALE, ROUNDING_MODE), 
                    accumulatedInterest.setScale(CURRENCY_SCALE, ROUNDING_MODE),
                    interestPaid.setScale(CURRENCY_SCALE, ROUNDING_MODE)
            ));

            previousDate = currentDate;
        }

        return InstallmentList;
    }

    /**
     * Valida as regras de negócio de data
     * @param request
     */
    private void validateBusinessRules(LoanRequestDto request) {
        if (request.finalDate().isBefore(request.startDate())) {
            throw new DateException("A data final deve ser maior que a data inicial");
        }
        if (request.firstPaymentDate().isBefore(request.startDate()) || request.firstPaymentDate().isAfter(request.finalDate())) {
            throw new DateException("A data de primeiro pagamento deve ser maior que a data inicial e menor que a data final");
        }
    }

    /**
     * Gera um conjunto ordenado e único de todas as datas relevantes para o cálculo.
     * @param startDate
     * @param finalDate
     * @param firstPaymentDate
     * @return
     */
    private SortedSet<LocalDate> generateEventDates(LocalDate startDate, LocalDate finalDate, LocalDate firstPaymentDate) {
        SortedSet<LocalDate> dates = new TreeSet<>();
        dates.add(startDate);

        LocalDate paymentDate = firstPaymentDate;
        while (!paymentDate.isAfter(finalDate)) {
            dates.add(paymentDate);
            
            if (firstPaymentDate.getDayOfMonth() == firstPaymentDate.with(TemporalAdjusters.lastDayOfMonth()).getDayOfMonth()) {
                paymentDate = paymentDate.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
            } else {
                paymentDate = paymentDate.plusMonths(1);
            }
        }
        dates.add(finalDate);

        LocalDate maturityDate = startDate.with(TemporalAdjusters.lastDayOfMonth());
        while (!maturityDate.isAfter(finalDate)) {
            dates.add(maturityDate);
            maturityDate = maturityDate.plusMonths(1).with(TemporalAdjusters.lastDayOfMonth());
        }

        return dates;
    }
}
