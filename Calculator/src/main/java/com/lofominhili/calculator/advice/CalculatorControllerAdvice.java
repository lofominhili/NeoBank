package com.lofominhili.calculator.advice;

import com.lofominhili.calculator.dto.basic.ErrorMessage;
import com.lofominhili.calculator.util.MessageHelper;
import com.lofominhili.calculator.util.exception.CalculationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class CalculatorControllerAdvice extends BaseControllerAdvice {

    public CalculatorControllerAdvice(MessageHelper messageHelper) {
        super(messageHelper);
    }

    @ExceptionHandler(CalculationException.class)
    public ResponseEntity<ErrorMessage> handleException(CalculationException exception) {
        var status = switch (exception.getCode()) {
            case INTERNAL_ERROR -> HttpStatus.INTERNAL_SERVER_ERROR;
            case CREDIT_ACCESS_DENIED -> HttpStatus.FORBIDDEN;
        };

        var code = exception.getCode().toString();
        var userMessageProperty = exception.getCode().getUserMessageProperty();
        return formErrorMessage(exception, status, code, userMessageProperty);
    }

}
