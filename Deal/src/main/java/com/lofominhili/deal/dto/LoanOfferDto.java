package com.lofominhili.deal.dto;

import lombok.Builder;

import java.math.BigDecimal;
import java.util.UUID;

@Builder(toBuilder = true)
public record LoanOfferDto(

        UUID statementId,

        BigDecimal requestedAmount,

        BigDecimal totalAmount,

        int term,

        BigDecimal monthlyPayment,

        BigDecimal rate,

        boolean isInsuranceEnabled,

        boolean isSalaryClient
) {
}
