package com.lofominhili.deal.controller;


import com.lofominhili.deal.dto.FinishRegistrationRequestDto;
import com.lofominhili.deal.dto.LoanOfferDto;
import com.lofominhili.deal.dto.LoanStatementRequestDto;
import com.lofominhili.deal.dto.basic.SuccessDto;
import com.lofominhili.deal.service.DealService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Tag(name = "DealController", description = "Controller class that handles deal operations")
@RestController
@RequestMapping
@RequiredArgsConstructor
public class DealController {

    private final DealService dealService;

    @Operation(summary = "Endpoint for fetching loan offers and creating client and statement with them")
    @PostMapping(value = "/statement")
    public ResponseEntity<SuccessDto<List<LoanOfferDto>>> createOffers(@Valid @RequestBody LoanStatementRequestDto request) {
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessDto<>(
                        HttpStatus.CREATED.value(),
                        "create offers",
                        dealService.createOffers(request)
                ));
    }

    @Operation(summary = "Endpoint for updating statement with selected offer")
    @PostMapping(value = "/offer/select")
    public ResponseEntity<SuccessDto<String>> selectOffer(@Valid @RequestBody LoanOfferDto loanOffer) {
        dealService.updateStatementWithOffer(loanOffer);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessDto<>(
                        HttpStatus.OK.value(),
                        "select offer",
                        "Statement successfully updated!"
                ));
    }

    @Operation(summary = "Endpoint for finishing registration(updating client, statement and create credit) with FinishRegistrationRequest and statementId")
    @PostMapping(value = "/calculate/{statementId}")
    public ResponseEntity<SuccessDto<String>> finishRegistration(@Valid @RequestBody FinishRegistrationRequestDto request, @PathVariable String statementId) {
        dealService.finishRegistration(request, statementId);
        return ResponseEntity.status(HttpStatus.OK)
                .body(new SuccessDto<>(
                        HttpStatus.OK.value(),
                        "finish registration",
                        "Registration successfully finished!"
                ));
    }
}
