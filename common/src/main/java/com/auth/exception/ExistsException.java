package com.auth.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class ExistsException extends BaseException {
    private static final int CODE = 400;

    public ExistsException(){
        this("资源已经存在");
    }

    public ExistsException(String message) {
        super(message);
    }


    public int getCode() {
        return CODE;
    }
}
