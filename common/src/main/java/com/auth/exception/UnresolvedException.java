package com.auth.exception;


public class UnresolvedException extends BaseException {
    private static final int CODE = 466;
    public UnresolvedException(){
        super("点位路径解析失败");
    }

    public UnresolvedException(String message, Throwable cause) {
        super(message,cause);
    }

    public int getCode() {
        return CODE;
    }
}
