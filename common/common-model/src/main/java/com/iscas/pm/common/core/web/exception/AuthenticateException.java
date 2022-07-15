package com.iscas.pm.common.core.web.exception;

import com.iscas.pm.common.core.model.AuthConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 身份认证异常
 */
@ResponseStatus(value= HttpStatus.UNAUTHORIZED)
public class AuthenticateException extends BaseException {
    private static final int CODE = 401;
    public AuthenticateException(){
        this(AuthConstants.AUTHORIZATION_EXCEPTION_MESSAGE);
    }

    public AuthenticateException(String message) {
        super(message);
    }

    public int getCode() {
        return CODE;
    }
}
