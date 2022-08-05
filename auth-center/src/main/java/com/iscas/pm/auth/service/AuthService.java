package com.iscas.pm.auth.service;

import com.iscas.pm.auth.model.AuthToken;

/**
 * @author 李昶
 * @Date: 20122/7/14 16:42
 * @Description: 用户认证service
 */
public interface AuthService {

    AuthToken login(String username, String password);

    boolean logout(String token);
}
