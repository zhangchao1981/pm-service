package com.auth.exception;


public class UnknowException extends BaseException {
    private static final int CODE = 490;
    public UnknowException(){
        super("服务器响应异常");
    }

    public UnknowException(String message, Throwable cause) {
        super(message,cause);
    }

    public int getCode() {
        return CODE;
    }
}
