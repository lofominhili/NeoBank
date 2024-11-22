package com.lofominhili.calculator.dto;

import java.math.BigDecimal;
import java.util.UUID;

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
