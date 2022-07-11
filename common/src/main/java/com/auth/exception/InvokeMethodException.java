package com.auth.exception;


public class InvokeMethodException extends BaseException {
    private static final int CODE = 461;

    public InvokeMethodException(String message,Throwable cause) {
        super(message,cause);
    }

    public int getCode() {
        return CODE;
    }
}
