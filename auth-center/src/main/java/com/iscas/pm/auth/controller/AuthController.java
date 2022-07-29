package com.iscas.pm.auth.controller;

import com.iscas.pm.auth.domain.UserLoginParam;
import com.iscas.pm.auth.domain.UserInfo;
import com.iscas.pm.auth.service.AuthService;
import com.iscas.pm.auth.service.UserService;
import com.iscas.pm.auth.utils.AuthToken;

import com.iscas.pm.common.core.model.UserDetailInfo;
import com.iscas.pm.common.core.util.RedisUtil;
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
    @Autowired
    private RedisUtil redisUtil;

    @ApiOperation(value = "用户登录",notes = "用户名密码登录，登录后需将token放到header里，key为:Authorization，value为:bearer +token(注意bearer后面有个空格) ")
    @PostMapping(value = "/login")
    public UserInfo login(@RequestBody @Valid UserLoginParam userLoginParam) {
        //申请token令牌
        AuthToken authToken = authService.login(userLoginParam.getUserName(), userLoginParam.getPassword());

        //存入redis   key=token  value="default"
       redisUtil.set(authToken.getAccess_token(),"default");

        //获取用户详细信息，包括权限列表
        UserDetailInfo userDetailInfo = userService.getUserDetails(userLoginParam.getUserName());

        //专为前端封装返回值，去除一些前端不用的信息
        UserInfo userInfo = new UserInfo();
        userInfo.setId(userDetailInfo.getUserId());
        userInfo.setUserName(userDetailInfo.getUsername());
        userInfo.setEmployeeName(userDetailInfo.getEmployeeName());
        userInfo.setAccessToken(authToken.getAccess_token());
        userInfo.setSystemPermissions(userDetailInfo.getSystemPermissions());
        userInfo.setProjectPermissions(userDetailInfo.getProjectPermissions());
        return userInfo;
    }

    @ApiOperation(value = "用户登出",notes = "退出系统，token失效")
    @GetMapping(value = "/logout")
    public void logout(@RequestHeader("Authorization") String token) {
        authService.logout(token);
    }

}
