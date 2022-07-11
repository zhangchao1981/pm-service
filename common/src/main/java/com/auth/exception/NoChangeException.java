package com.auth.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value= HttpStatus.NOT_MODIFIED)
public class NoChangeException extends BaseException{
    private static final int CODE = 304;

    public NoChangeException(){
        this("资源没有变化");
    }

    public NoChangeException(String message) {
        super(message);
    }


    public int getCode() {
        return CODE;
    }
}
