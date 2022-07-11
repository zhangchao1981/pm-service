package com.auth.exception;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@ResponseStatus(value= HttpStatus.FORBIDDEN)
public class AuthorizeException extends BaseException {
    private static final int CODE = 403;

    public AuthorizeException(){
        this("权限不足");
    }
    public AuthorizeException(String message) {
        super(message);
    }

    public int getCode() {
        return CODE;
    }
}
