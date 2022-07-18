package com.iscas.pm.gateway.exception;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 认证相关常量
 */
public class AuthConstants {
    private AuthConstants(){}
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String AUTHORIZATION_EXCEPTION_MESSAGE = "用户认证失败，请重新登录！";
}
