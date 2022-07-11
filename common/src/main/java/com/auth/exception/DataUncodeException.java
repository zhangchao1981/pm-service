package com.auth.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class DataUncodeException extends BaseException {
    private static final int CODE = 500;

    public DataUncodeException(){
        this("数据解密出错");
    }

    public DataUncodeException(String message, Exception e){
        super(message,e);
    }

    public DataUncodeException(String message) {
        super(message);
    }

    public DataUncodeException(Exception e) {
        super("数据解密出错",e);
    }


    public int getCode() {
        return CODE;
    }
}
