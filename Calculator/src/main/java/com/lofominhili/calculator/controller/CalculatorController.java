package com.lofominhili.calculator.controller;

import com.lofominhili.calculator.dto.CreditDto;
import com.lofominhili.calculator.dto.LoanOfferDto;
import com.lofominhili.calculator.dto.LoanStatementRequestDto;
import com.lofominhili.calculator.dto.ScoringDataDto;
import com.lofominhili.calculator.dto.basic.SuccessDto;
import com.lofominhili.calculator.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Tag(name = "CalculatorController", description = "Controller class that handles calculator operations")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class CalculatorController {

    private final LoanService loanService;

    @Operation(summary = "Endpoint for making 4 loan offers")
    @PostMapping(value = "/offers")
    public ResponseEntity<SuccessDto<List<LoanOfferDto>>> makeOffers(@Valid @RequestBody LoanStatementRequestDto request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessDto<>(
                        HttpStatus.CREATED.value(),
                        "make offers",
                        loanService.makeOffers(request)
                ));
    }

    @Operation(summary = "Endpoint for full calculation of loan parameters")
    @PostMapping(value = "/calc")
    public ResponseEntity<SuccessDto<CreditDto>> calcCredit(@Valid @RequestBody ScoringDataDto scoringData) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessDto<>(
                        HttpStatus.OK.value(),
                        "calc credit",
                        loanService.calculateCredit(scoringData)
                ));
    }

}
