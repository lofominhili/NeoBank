package com.lofominhili.calculator.advice;

import com.lofominhili.calculator.dto.basic.ErrorMessage;
import jakarta.validation.ConstraintViolationException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class ValidationControllerAdvice {

    private final String VALIDATION_ERROR_CODE = "VALIDATION_ERROR";

    @ExceptionHandler({BindException.class, ConstraintViolationException.class})
    public ResponseEntity<ErrorMessage> handleException(Exception exception) {
        return formErrorMessage(exception);
    }

    private ResponseEntity<ErrorMessage> formErrorMessage(Exception exception) {
        var args = new HashMap<String, Object>();

        if (exception instanceof BindException ex) {
            ex.getBindingResult().getFieldErrors().forEach(fieldError -> {
                args.put(fieldError.getField(), fieldError.getDefaultMessage());
            });
        } else if (exception instanceof ConstraintViolationException ex) {
            ex.getConstraintViolations().forEach(constraintViolation ->
                    args.put(constraintViolation.getPropertyPath().toString(), constraintViolation.getMessage())
            );
        }

        var errorMessage = ErrorMessage.builder()
                .code(VALIDATION_ERROR_CODE)
                .args(args)
                .userMessage("Данные указаны неправильно или с вашими данными вам нельзя выдать кредит")
                .build();

        var response = ResponseEntity
                .status(HttpStatus.BAD_REQUEST)
                .body(errorMessage);

        var validationErrorMessage = "A validation error occurred while contacting the server";
        log.error(VALIDATION_ERROR_CODE + ": " + validationErrorMessage + ": " + args, exception);
        return response;
    }
}