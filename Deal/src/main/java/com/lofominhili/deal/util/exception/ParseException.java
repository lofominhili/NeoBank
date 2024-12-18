package com.lofominhili.deal.util.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
public class ParseException extends BaseException {

    protected final Code code;

    protected ParseException(Code code, String msg, Throwable e) {
        super(msg, e);
        this.code = code;
    }

    @Getter
    @RequiredArgsConstructor
    public enum Code {

        PARSING_ERROR("error-message.parsing-error");

        private final String userMessageProperty;

        public ParseException get(String msg) {
            return new ParseException(this, msg, null);
        }

        public ParseException get(Throwable e, String msg) {
            return new ParseException(this, msg, e);
        }
    }
}
