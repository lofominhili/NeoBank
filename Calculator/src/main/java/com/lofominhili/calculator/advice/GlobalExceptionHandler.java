package com.lofominhili.calculator.advice;

import com.lofominhili.calculator.dto.basic.ErrorMessage;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.method.annotation.MethodArgumentTypeMismatchException;

import java.util.HashMap;

@Slf4j
@RequiredArgsConstructor
@RestControllerAdvice
public class GlobalExceptionHandler {

    private final String GLOBAL_ERROR_CODE = "GLOBAL_ERROR";

    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ResponseEntity<ErrorMessage> handleException(HttpRequestMethodNotSupportedException ex) {
        var args = new HashMap<String, Object>();
        args.put(ex.getLocalizedMessage(), "Неправильный тип метода запроса");

        var errorMessage = setErrorMessage(args);

        return setResponse(errorMessage, HttpStatus.METHOD_NOT_ALLOWED);
    }

    @ExceptionHandler(HttpMessageNotReadableException.class)
    public ResponseEntity<ErrorMessage> handleException(HttpMessageNotReadableException ex) {
        var args = new HashMap<String, Object>();
        args.put(ex.getLocalizedMessage(), "Ошибка парсинга json");

        var errorMessage = setErrorMessage(args);

        return setResponse(errorMessage, HttpStatus.BAD_REQUEST);
    }

    @ExceptionHandler(MethodArgumentTypeMismatchException.class)
    public ResponseEntity<ErrorMessage> handleException(MethodArgumentTypeMismatchException ex) {
        var args = new HashMap<String, Object>();
        args.put(ex.getLocalizedMessage(), "Неправильный тип аргумента");

        var errorMessage = setErrorMessage(args);

        return setResponse(errorMessage, HttpStatus.BAD_REQUEST);
    }

    private ErrorMessage setErrorMessage(HashMap<String, Object> args) {
        return ErrorMessage.builder()
                .code(GLOBAL_ERROR_CODE)
                .args(args)
                .build();
    }

    private ResponseEntity<ErrorMessage> setResponse(ErrorMessage errorMessage, HttpStatus status) {
        return ResponseEntity
                .status(status)
                .body(errorMessage);
    }
}

