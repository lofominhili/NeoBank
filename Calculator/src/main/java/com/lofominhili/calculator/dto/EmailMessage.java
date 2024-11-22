package com.lofominhili.calculator.dto;

import com.lofominhili.calculator.util.enumeration.Theme;

public record EmailMessage(

        String address,

        Theme theme,

        long statementId,

        String text
) {
}
