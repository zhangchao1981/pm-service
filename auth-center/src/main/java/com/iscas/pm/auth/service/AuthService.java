package com.iscas.pm.auth.service;

import com.iscas.pm.auth.utils.AuthToken;

/**
 * @author 李昶
 * @Date: 20122/7/14 16:42
 * @Description: 用户认证service
 */
public interface AuthService {

    /***
     * 授权认证方法
     */
    AuthToken login(String username, String password);

    boolean logout(String token);
}
