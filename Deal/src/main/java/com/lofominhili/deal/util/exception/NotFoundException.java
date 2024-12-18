package com.lofominhili.deal.util.exception;


import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class NotFoundException extends BaseException {

    protected final Code code;

    protected NotFoundException(Code code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Code {

        STATEMENT_NOT_FOUND("error-message.statement-not-found");

        private final String userMessageProperty;

        public NotFoundException get(String msg) {
            return new NotFoundException(this, msg, null);
        }

        public NotFoundException get(Throwable e, String msg) {
            return new NotFoundException(this, msg, e);
        }
    }
}
