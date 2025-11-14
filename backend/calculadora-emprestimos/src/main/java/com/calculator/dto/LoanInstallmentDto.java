package com.calculator.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record LoanInstallmentDto(LocalDate effectiveDate,
                                 BigDecimal loanAmount, 
                                 BigDecimal outstandingBalance, 
                                 String consolidated, 
                                 BigDecimal totalPayment, 
                                 BigDecimal amortization, 
                                 BigDecimal principalBalance, 
                                 BigDecimal provision, 
                                 BigDecimal accumulated, 
                                 BigDecimal paid) {}
