package com.auth.exception;

import lombok.Getter;
import lombok.Setter;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

@Getter
@Setter
@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class DecryptException extends BaseException {
    private static final int CODE = 500;

    public DecryptException(){
        this("数据解密出错");
    }

    public DecryptException(String message,Exception e){
        super(message,e);
    }

    public DecryptException(String message) {
        super(message);
    }


    public int getCode() {
        return CODE;
    }
}
