package com.lofominhili.calculator.dto;

import java.math.BigDecimal;
import java.time.LocalDate;

public record PaymentScheduleElementDto(

        int number,

        LocalDate date,

        BigDecimal totalPayment,

        BigDecimal interestPayment,

        BigDecimal debtPayment,

        BigDecimal remainingDebt
) {
}