package com.lofominhili.calculator.service;

import com.lofominhili.calculator.dto.CreditDto;
import com.lofominhili.calculator.dto.LoanOfferDto;
import com.lofominhili.calculator.dto.LoanStatementRequestDto;
import com.lofominhili.calculator.dto.ScoringDataDto;
import org.jetbrains.annotations.NotNull;

import java.util.List;

public interface LoanService {

    @NotNull
    List<LoanOfferDto> makeOffers(@NotNull final LoanStatementRequestDto loanStatementRequestDto);

    @NotNull
    CreditDto calculateCredit(@NotNull final ScoringDataDto scoringData);
}