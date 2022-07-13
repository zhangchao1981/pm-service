package com.user.controller;

import com.entity.Result;
import com.entity.StatusCode;
import com.user.service.AuthService;
import com.user.utils.AuthToken;
import com.user.utils.CookieUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.servlet.http.HttpServletResponse;


/*****
 * @Date: 2019/7/7 16:42
 * @Description: com.changgou.oauth.controller
 ****/
@RestController
@RequestMapping(value = "/authxx")
public class AuthController {

    //客户端ID
    @Value("${auth.clientId}")
    private String clientId;

    //秘钥
    @Value("${auth.clientSecret}")
    private String clientSecret;

    //Cookie存储的域名
    @Value("${auth.cookieDomain}")
    private String cookieDomain;

    //Cookie生命周期
    @Value("${auth.cookieMaxAge}")
    private int cookieMaxAge;

    @Autowired
    AuthService authService;

    @RequestMapping(value = "/login")
    public Result login(String username, String password) {
        if (StringUtils.isEmpty(username)) {
            throw new RuntimeException("用户名不允许为空");
        }
        if (StringUtils.isEmpty(password)) {
            throw new RuntimeException("密码不允许为空");
        }
        //申请令牌
        AuthToken authToken = authService.login(username, password, clientId, clientSecret);

        if (authToken != null) {
            //用户身份令牌
            String access_token = authToken.getAccessToken();
            //将令牌存储到cookie     是否需要？
            saveCookie(access_token);
            return new Result(true, StatusCode.OK, "登录成功！", access_token);
        }
        return new Result(false, StatusCode.ERROR, "登录失败");
    }

    /***
     * 将令牌存储到cookie
     * @param token
     */
    private void saveCookie(String token) {
        HttpServletResponse response = ((ServletRequestAttributes) RequestContextHolder.getRequestAttributes()).getResponse();
        CookieUtil.addCookie(response, cookieDomain, "/", "Authorization", token, cookieMaxAge, false);
    }
}
