package com.iscas.pm.auth.controller;



import com.iscas.pm.auth.service.AuthService;
import com.iscas.pm.auth.utils.AuthToken;
import com.iscas.pm.common.core.web.exception.AuthorizeException;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.constraints.NotEmpty;

/*****
 * @author 李昶
 * @Date: 20122/7/14 16:42
 * @Description: oauth认证的login请求入口
 ****/
@RestController
@RequestMapping(value = "/authxx")
@Api(tags = {"认证管理"})
public class AuthController {

    //客户端ID
    @Value("${auth.clientId}")
    private String clientId;

    //秘钥
    @Value("${auth.clientSecret}")
    private String clientSecret;


    @Autowired
    AuthService authService;


    @ApiOperation(value = "用户登录(账号，密码)")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "username", value = "用户名", dataType = "String"),
            @ApiImplicitParam(name = "password", value = "密码", dataType = "String")})
    @RequestMapping(value = "/login")
    public String login(@NotEmpty(message = "用户名不允许为空") String username, @NotEmpty(message = "密码不允许为空") String password) {
        //申请令牌
        AuthToken authToken = authService.login(username, password, clientId, clientSecret);
        if (authToken == null) {
            throw new AuthorizeException("令牌申请失败");
        }
        //用户身份令牌
        String access_token = authToken.getAccessToken();
        return access_token;
    }
}
