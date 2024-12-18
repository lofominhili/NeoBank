package com.lofominhili.deal.util.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ServiceException extends BaseException {

    protected final Code code;

    protected ServiceException(Code code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Code {

        CALCULATOR_SERVICE_ERROR("error-message.calculator-service-error");

        private final String userMessageProperty;

        public ServiceException get(String msg) {
            return new ServiceException(this, msg, null);
        }

        public ServiceException get(Throwable e, String msg) {
            return new ServiceException(this, msg, e);
        }
    }
}
