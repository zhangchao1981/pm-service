package com.auth.exception;

import lombok.Data;
import org.springframework.http.HttpStatus;

@Data
public class SimpleBaseException extends RuntimeException {

    private int code;

    private String message;

    public SimpleBaseException(int code,String message){
        this.code=code;
        this.message=message;
    }

    public SimpleBaseException(Remind remind){
        this.code = remind.getCode();
        this.message=remind.getMsg();
    }

    public SimpleBaseException(Exception e){
        super(e);
        this.code = HttpStatus.INTERNAL_SERVER_ERROR.value();
        this.message=e.getMessage();
    }

}
