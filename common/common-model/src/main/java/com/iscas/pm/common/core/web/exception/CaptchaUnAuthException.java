package com.iscas.pm.common.core.web.exception;

import com.iscas.pm.common.core.model.AuthConstants;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.ResponseStatus;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 验证码校验异常
 */
@ResponseStatus(value= HttpStatus.BAD_REQUEST)
public class CaptchaUnAuthException extends BaseException {
    private static final int CODE = 10401;
    public CaptchaUnAuthException(){
        this(AuthConstants.CAPTCHA_EXCEPTION_MESSAGE);
    }

    public CaptchaUnAuthException(String message) {
        super(message);
    }

    public int getCode() {
        return CODE;
    }
}
