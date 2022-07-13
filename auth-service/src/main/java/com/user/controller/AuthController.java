package com.user.controller;


import com.entity.Result;
import com.entity.StatusCode;
import com.user.service.AuthService;
import com.user.utils.AuthToken;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.redis.core.RedisTemplate;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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


    @Autowired
    AuthService authService;

    @Autowired
    private RedisTemplate redisTemplate;


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
            //用户身份令牌   (已经是JWT令牌了)
            String access_token = authToken.getAccessToken();
            return new Result(true, StatusCode.OK, "登录成功！", access_token);
        }
        return new Result(false, StatusCode.ERROR, "登录失败");
    }
}
