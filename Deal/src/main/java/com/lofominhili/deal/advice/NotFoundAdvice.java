package com.lofominhili.deal.advice;

import com.lofominhili.deal.dto.basic.ErrorMessage;
import com.lofominhili.deal.util.MessageHelper;
import com.lofominhili.deal.util.exception.NotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import static org.springframework.http.HttpStatus.NOT_FOUND;

@RestControllerAdvice
public class NotFoundAdvice extends BaseControllerAdvice {

    public NotFoundAdvice(MessageHelper messageHelper) {
        super(messageHelper);
    }

    @ExceptionHandler(NotFoundException.class)
    public ResponseEntity<ErrorMessage> handleException(NotFoundException exception) {
        var status = switch (exception.getCode()) {
            case STATEMENT_NOT_FOUND -> NOT_FOUND;

        };
        var code = exception.getCode().toString();
        var userMessageProperty = exception.getCode().getUserMessageProperty();
        return formErrorMessage(exception, status, code, userMessageProperty);
    }
}
