package com.lofominhili.deal.service;

import com.lofominhili.deal.dto.FinishRegistrationRequestDto;
import com.lofominhili.deal.dto.LoanOfferDto;
import com.lofominhili.deal.dto.LoanStatementRequestDto;
import jakarta.validation.constraints.NotNull;

import java.util.List;

public interface DealService {

    @NotNull
    List<LoanOfferDto> createOffers(@NotNull final LoanStatementRequestDto loanStatementRequest);

    void updateStatementWithOffer(@NotNull final LoanOfferDto loanOfferDto);

    void finishRegistration(@NotNull final FinishRegistrationRequestDto finishRegistrationRequest, @NotNull final String statementId);

}
