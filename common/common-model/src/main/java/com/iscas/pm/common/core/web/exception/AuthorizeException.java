package com.iscas.pm.common.core.web.exception;

import com.iscas.pm.common.core.model.AuthConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 鉴权异常
 */
@ResponseStatus(value= HttpStatus.FORBIDDEN)
public class AuthorizeException extends BaseException {
    private static final int CODE = 403;

    public AuthorizeException(){
        this(AuthConstants.AUTHORIZE_EXCEPTION_MESSAGE);
    }

    public AuthorizeException(String message) {
        super(message);
    }

    public int getCode() {
        return CODE;
    }
}
