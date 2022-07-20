package com.iscas.pm.auth.controller;

import com.iscas.pm.auth.domain.UserLoginParam;
import com.iscas.pm.auth.service.AuthService;
import com.iscas.pm.auth.utils.AuthToken;
import com.iscas.pm.common.core.web.exception.AuthorizeException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/*****
 * @author 李昶
 * @Date: 20122/7/14 16:42
 * @Description: oauth认证的login请求入口
 ****/
@RestController
@RequestMapping(value = "/auth")
@Api(tags = {"认证管理"})
public class AuthController {

    @Autowired
    AuthService authService;

    @ApiOperation(value = "用户登录")
    @PostMapping(value = "/login")
    public String login(@RequestBody @Valid UserLoginParam userLoginParam) {
        //申请令牌
        AuthToken authToken = authService.login(userLoginParam.getUserName(), userLoginParam.getPassword());
        if (authToken == null) {
            throw new AuthorizeException("令牌申请失败");
        }
        //用户身份令牌
        String access_token = authToken.getAccessToken();
        return access_token;
    }
}
