package com.lofominhili.calculator.dto;

import com.lofominhili.calculator.util.enumeration.ChangeType;
import com.lofominhili.calculator.util.enumeration.StatementStatus;

import java.time.LocalDateTime;

public record StatementStatusHistoryDto(

        StatementStatus status,

        LocalDateTime time,

        ChangeType changetype
) {
}
