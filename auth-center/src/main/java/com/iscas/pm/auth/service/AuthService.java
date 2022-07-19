package com.iscas.pm.auth.service;


import com.iscas.pm.auth.utils.AuthToken;

/*****
 * @Date: 2019/7/7 16:23
 * @Description: com.changgou.oauth.service
 ****/
public interface AuthService {

    /***
     * 授权认证方法
     */
    AuthToken login(String username, String password);
}
