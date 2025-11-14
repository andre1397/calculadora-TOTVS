package com.calculator.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;


public record LoanRequestDto(@NotNull LocalDate startDate, 
                             @NotNull LocalDate finalDate, 
                             @NotNull LocalDate firstPaymentDate, 
                             @Positive BigDecimal loanAmount, 
                             @Positive BigDecimal interestRate) {}
