package com.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.UNAUTHORIZED)
public class AuthenticateException extends BaseException {
    private static final int CODE = 401;
    public AuthenticateException(){
        this("用户名或密码错误");
    }

    public AuthenticateException(String message) {
        super(message);
    }

    public int getCode() {
        return CODE;
    }
}
