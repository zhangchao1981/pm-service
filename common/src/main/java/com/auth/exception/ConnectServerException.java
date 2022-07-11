package com.auth.exception;


public class ConnectServerException extends BaseException {
    private static final int CODE = 460;
    public ConnectServerException(){
        super("连接服务器失败");
    }

    public ConnectServerException(String message,Throwable cause) {
        super(message,cause);
    }

    public int getCode() {
        return CODE;
    }
}
