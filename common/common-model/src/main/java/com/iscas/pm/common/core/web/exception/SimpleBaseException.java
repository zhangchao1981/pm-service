package com.iscas.pm.common.core.web.exception;

import lombok.Data;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 自定义code异常
 */
@Data
public class SimpleBaseException extends RuntimeException {

    private int code;
    private String message;

    public SimpleBaseException(int code, String message){
        this.code=code;
        this.message=message;
    }

}
