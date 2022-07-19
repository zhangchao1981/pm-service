package com.iscas.pm.auth.controller;

import com.iscas.pm.auth.domain.User;
import com.iscas.pm.auth.domain.UserLogin;
import com.iscas.pm.auth.service.LoginService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;

/**
 * @Author： zhangchao
 * @Date： 2022/7/19
 * @Description： 登录登出处理类
 */
@Slf4j
@RestController
@RequestMapping("/auth")
@Api(tags = {"登录相关"})
public class LoginController {
    @Autowired
    private LoginService loginService;

    @ApiOperation(value = "用户登录")
    @PostMapping("/login")
    public User login(@RequestBody UserLogin user, HttpServletRequest request, HttpServletResponse response) {
        String username = user.getUsername();


        // 调用实际登录接口
        User userLogin = loginService.login(username, user.getPassword(), response);
        return userLogin;
    }

    @ApiOperation(value = "用户登出")
    @GetMapping("/logout")
    public void logout(@RequestHeader("Authorization") String token) {
        loginService.logout(token);
    }

    @ApiOperation(value = "token认证",notes = "网关调用")
    @GetMapping("/token/{token}")
    public User check(@PathVariable String token,@RequestParam(required = false) Boolean refreshExpiredTime,@RequestParam("type")String type){
        return loginService.check(token,refreshExpiredTime,type);
    }

}

