package com.iscas.pm.common.model;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 认证相关常量
 */
public class AuthConstants {
    private AuthConstants(){}
    public static final String AUTHORIZATION_HEADER = "Authorization";
    public static final String CURRENT_PROJECT_ID = "currentProjectId";
    public static final String USER_ID = "userId";
    public static final String USER_NAME = "userName";
    public static final String FROM_IP = "fromIP";
    public static final String AUTHORIZATION_EXCEPTION_MESSAGE = "用户认证失败，请重新登录！";
    public static final String CAPTCHA_EXCEPTION_MESSAGE = "未先通过验证码校验！";
    public static final String AUTHORIZE_EXCEPTION_MESSAGE = "你没有权限访问该部分资源！";
}
