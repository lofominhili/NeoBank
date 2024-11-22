package com.lofominhili.calculator.util.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class CalculationException extends BaseException {

    protected final Code code;

    private CalculationException(Code code, Throwable e, String msg) {
        super(msg, e);
        this.code = code;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Code {

        INTERNAL_ERROR("error-message.internal-error"),
        CREDIT_ACCESS_DENIED("error-message.credit-access-denied"),
        ;

        private final String userMessageProperty;

        public CalculationException get(String msg) {
            return new CalculationException(this, null, msg);
        }

        public CalculationException get(Throwable e, String msg) {
            return new CalculationException(this, e, msg);
        }
    }
}
