package com.iscas.pm.auth.controller;

import com.iscas.pm.auth.domain.UserLoginParam;
import com.iscas.pm.auth.domain.user.UserInfo;
import com.iscas.pm.auth.service.AuthService;
import com.iscas.pm.auth.service.UserService;
import com.iscas.pm.auth.utils.AuthToken;

import com.iscas.pm.common.core.model.UserDetailInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * @author 李昶
 * @Date: 20122/7/14 16:42
 * @Description: 用户认证管理
 */
@RestController
@RequestMapping(value = "/auth")
@Api(tags = {"认证管理"})
public class AuthController {

    @Autowired
    private AuthService authService;
    @Autowired
    private UserService userService;

    @ApiOperation(value = "用户登录",notes = "用户名密码登录，密码暂且明文方式，后面改为加密方式")
    @PostMapping(value = "/login")
    public UserInfo login(@RequestBody @Valid UserLoginParam userLoginParam) {
        //申请token令牌
        AuthToken authToken = authService.login(userLoginParam.getUserName(), userLoginParam.getPassword());

        //获取访问token
        String access_token = authToken.getAccessToken();

        UserDetailInfo userDetailInfo = userService.getUserDetails(userLoginParam.getUserName(),"default");

        UserInfo userInfo = new UserInfo();
        userInfo.setId(userDetailInfo.getUserId());
        userInfo.setUserName(userDetailInfo.getUsername());
        userInfo.setEmployeeName(userDetailInfo.getEmployeeName());
        userInfo.setAccessToken(access_token);
        userInfo.setPermissions(userDetailInfo.getPermissions());
        return userInfo;
    }

    @ApiOperation(value = "用户登出",notes = "退出系统，token失效")
    @GetMapping(value = "/logout")
    public void logout() {

    }
}
