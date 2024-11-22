package com.lofominhili.calculator.configuration;

import org.springframework.boot.context.properties.ConfigurationProperties;

import java.math.BigDecimal;

@ConfigurationProperties(prefix = "loan")
public record TestProperties(

        BigDecimal baseRate,

        BigDecimal insuranceDiscount,

        BigDecimal salaryClientDiscount,

        BigDecimal insuranceCost
) {
}
