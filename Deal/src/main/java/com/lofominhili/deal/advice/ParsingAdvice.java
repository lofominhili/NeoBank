package com.lofominhili.deal.advice;

import com.lofominhili.deal.dto.basic.ErrorMessage;
import com.lofominhili.deal.util.MessageHelper;
import com.lofominhili.deal.util.exception.ParseException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ParsingAdvice extends BaseControllerAdvice {

    public ParsingAdvice(MessageHelper messageHelper) {
        super(messageHelper);
    }

    @ExceptionHandler(ParseException.class)
    public ResponseEntity<ErrorMessage> handleServiceException(ParseException exception) {
        var status = switch (exception.getCode()) {
            case PARSING_ERROR -> HttpStatus.BAD_REQUEST;
        };

        var code = exception.getCode().toString();
        var userMessageProperty = exception.getCode().getUserMessageProperty();
        return formErrorMessage(exception, status, code, userMessageProperty);
    }
}
