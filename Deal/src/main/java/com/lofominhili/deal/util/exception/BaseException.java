package com.lofominhili.deal.util.exception;

import lombok.Getter;

import java.util.Map;

@Getter
public class BaseException extends RuntimeException {

    protected Map<String, Object> args;

    protected String messageParam;

    protected BaseException(String msg, Throwable e) {
        super(msg, e);
    }

    public BaseException args(Map<String, Object> args) {
        this.args = args;
        return this;
    }

    public BaseException messageParam(String messageParam) {
        this.messageParam = messageParam;
        return this;
    }

    public String getArgsAsString() {
        if (this.args == null) {
            return "";
        }

        return args.toString();
    }
}