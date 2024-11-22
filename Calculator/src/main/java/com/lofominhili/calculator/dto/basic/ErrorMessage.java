package com.lofominhili.calculator.dto.basic;

import lombok.*;

import java.util.Map;

@Getter
@Setter
@ToString
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class ErrorMessage {

    private Map<String, Object> args;

    private String code;

    private String userMessage;

    private String devMessage;
}
