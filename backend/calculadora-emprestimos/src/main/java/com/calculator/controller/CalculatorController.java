package com.calculator.controller;

import java.util.List;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.calculator.dto.LoanInstallmentDto;
import com.calculator.dto.LoanRequestDto;
import com.calculator.service.CalculatorService;

import jakarta.validation.Valid;

@RestController
@RequestMapping("/api")
public class CalculatorController {

    private final CalculatorService calculatorService;

    public CalculatorController(CalculatorService calculatorService){
        this.calculatorService = calculatorService;
    }

    @PostMapping("/calculate")
    public ResponseEntity<List<LoanInstallmentDto>> calculate(@Valid @RequestBody LoanRequestDto request){
        List<LoanInstallmentDto> InstallmentList = calculatorService.calculate(request);
        return ResponseEntity.ok(InstallmentList);
    }

}
