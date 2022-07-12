package com.iscas.pm.common.model.exception;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 异常基础类
 */
public abstract class BaseException extends RuntimeException{

    public BaseException(String message) {
        super(message);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }

    public abstract int getCode();
}
