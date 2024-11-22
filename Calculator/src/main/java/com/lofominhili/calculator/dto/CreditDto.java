package com.lofominhili.calculator.dto;

import java.math.BigDecimal;
import java.util.List;

public record CreditDto(

        BigDecimal amount,

        int term,

        BigDecimal monthlyPayment,

        BigDecimal rate,

        BigDecimal psk,

        boolean isInsuranceEnabled,

        boolean isSalaryClient,

        List<PaymentScheduleElementDto> paymentSchedule
) {
}
