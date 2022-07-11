package com.auth.exception;


public class SessionExpireException extends BaseException {
    private static final int CODE = 462;
    public SessionExpireException(){
        super("会话过期");
    }

    public SessionExpireException(String message,Throwable cause) {
        super(message,cause);
    }

    public int getCode() {
        return CODE;
    }
}
