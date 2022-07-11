package com.auth.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value= HttpStatus.NOT_FOUND)
public class NotFoundException extends BaseException {
    private static final int CODE = 400;

    private static final String DEFAULT_MESSAGE = "资源未找到";

    public NotFoundException(){
        this(DEFAULT_MESSAGE, null);
    }
    public NotFoundException(String message) {
        this(message, null);
    }

    public NotFoundException(Throwable t) {
        this(DEFAULT_MESSAGE, t);
    }

    public NotFoundException(String message, Throwable t) {
        super(message, t);
    }

    public int getCode() {
        return CODE;
    }
}
