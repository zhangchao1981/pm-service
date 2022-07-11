package com.auth.exception;


public class NetworkException extends BaseException {
    private static final int CODE = 465;
    public NetworkException(){
        super("服务器响应异常");
    }

    public NetworkException(String message, Throwable cause) {
        super(message,cause);
    }

    public int getCode() {
        return CODE;
    }
}
