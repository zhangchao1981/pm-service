package com.auth.exception;


public class ResponseException extends BaseException {
    private static final int CODE = 463;
    public ResponseException(){
        super("服务器响应异常");
    }

    public ResponseException(String message, Throwable cause) {
        super(message,cause);
    }

    public int getCode() {
        return CODE;
    }
}
