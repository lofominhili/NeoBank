package com.lofominhili.deal.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;

import java.time.Duration;

import static java.time.temporal.ChronoUnit.SECONDS;

@Configuration
public class RestTemplateConfiguration {

    @Bean
    public RestTemplate calculatorRestTemplate(
            @Value("${internal.service.calculator.url}") String calculatorUri,
            RestTemplateResponseErrorHandler restTemplateResponseErrorHandler,
            RestTemplateBuilder builder
    ) {
        return builder
                .rootUri(calculatorUri)
                .connectTimeout(Duration.of(5, SECONDS))
                .errorHandler(restTemplateResponseErrorHandler)
                .build();
    }
}