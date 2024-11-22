package com.lofominhili.calculator.dto.basic;

public record SuccessDto<T>(

        int statusCode,

        String subject,

        T data
) {
}