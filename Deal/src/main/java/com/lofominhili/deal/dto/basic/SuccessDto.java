package com.lofominhili.deal.dto.basic;

public record SuccessDto<T>(

        int statusCode,

        String subject,

        T data
) {
}
