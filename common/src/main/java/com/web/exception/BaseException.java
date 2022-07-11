package com.web.exception;

/**
 * 基础异常类
 *
 * @author xuzhengfan
 */
public abstract class BaseException extends RuntimeException {
    private Integer statusCode;

    public BaseException(Integer statusCode) {
        this.statusCode = statusCode;
    }

    public BaseException(Integer statusCode, String message) {
        super(message);
        this.statusCode = statusCode;
    }

    public BaseException(Integer statusCode, String message, Throwable cause) {
        super(message, cause);
        this.statusCode = statusCode;
    }

    public BaseException(Throwable cause, Integer statusCode) {
        super(cause);
        this.statusCode = statusCode;
    }

    public Integer getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(Integer statusCode) {
        this.statusCode = statusCode;
    }
}