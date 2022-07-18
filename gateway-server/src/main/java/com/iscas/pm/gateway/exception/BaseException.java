package com.iscas.pm.gateway.exception;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 异常基础类
 */
public abstract class BaseException extends RuntimeException{

    public BaseException(String message) {
        super(message);
    }

    public abstract int getCode();
}
