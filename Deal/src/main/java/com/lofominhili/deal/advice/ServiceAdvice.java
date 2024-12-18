package com.lofominhili.deal.advice;

import com.lofominhili.deal.dto.basic.ErrorMessage;
import com.lofominhili.deal.util.MessageHelper;
import com.lofominhili.deal.util.exception.ServiceException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

@RestControllerAdvice
public class ServiceAdvice extends BaseControllerAdvice {

    public ServiceAdvice(MessageHelper messageHelper) {
        super(messageHelper);
    }

    @ExceptionHandler(ServiceException.class)
    public ResponseEntity<ErrorMessage> handleServiceException(ServiceException exception) {
        var status = switch (exception.getCode()) {
            case CALCULATOR_SERVICE_ERROR -> HttpStatus.SERVICE_UNAVAILABLE;
        };

        var code = exception.getCode().toString();
        var userMessageProperty = exception.getCode().getUserMessageProperty();
        return formErrorMessage(exception, status, code, userMessageProperty);
    }
}
