package com.web.exception;

/**
 * @author xuzhengfan on 2021/9/23
 */
public class CaptchaUnAuthException extends BaseException {
    public static final Integer STATUS_CODE = 10401;

    public CaptchaUnAuthException() {
        super(STATUS_CODE, "未先通过验证码认证");
    }

    public CaptchaUnAuthException(Integer statusCode) {
        super(statusCode);
    }

    public CaptchaUnAuthException(String message) {
        super(STATUS_CODE, message);
    }

    public CaptchaUnAuthException(Integer statusCode, String message) {
        super(statusCode, message);
    }

    public CaptchaUnAuthException(Integer statusCode, String message, Throwable cause) {
        super(statusCode, message, cause);
    }

    public CaptchaUnAuthException(Throwable cause, Integer statusCode) {
        super(cause, statusCode);
    }
}
