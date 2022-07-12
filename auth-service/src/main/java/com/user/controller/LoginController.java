//package com.user.controller;
//
//
//
//import com.entity.Result;
//import com.entity.StatusCode;
//import com.user.domain.User;
//import com.user.domain.UserLogin;
//import com.user.service.LoginService;
//import com.user.utils.AuthToken;
//import com.util.CaptchaConstant;
//import com.util.RedisKeyConstants;
//import com.util.RedisUtil;
//import com.util.Wrapper;
//import com.web.exception.CaptchaUnAuthException;
//import io.swagger.annotations.Api;
//import io.swagger.annotations.ApiOperation;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.beans.factory.annotation.Value;
//import org.springframework.web.bind.annotation.PostMapping;
//import org.springframework.web.bind.annotation.RequestBody;
//import org.springframework.web.bind.annotation.RequestMapping;
//import org.springframework.web.bind.annotation.RestController;
//
//import javax.annotation.Resource;
//import javax.servlet.http.HttpServletRequest;
//import javax.servlet.http.HttpServletResponse;
//import java.util.Objects;
//
//@Wrapper
//@Slf4j
//@RestController
//@RequestMapping("/auth")
//@Api(tags = {"登录相关"})
//public class LoginController {
//    @Resource
//    private LoginService loginService;
//    @Autowired
//    private RedisUtil redisUtil;
//
//
//    /**
//     * 参数传递：
//     * 1.	账号
//     * 2.	密码
//     * 3.	授权方式
//     * 请求头传递
//     */
//    //客户端ID
//    @Value("${auth.clientId}")
//    private String clientId;
//    //客户端秘钥
//    @Value("${auth.clientSecret}")
//    private String clientSecret;
//
//    @Autowired
//    private LoginService userLoginService;
////    @Autowired
////    private LoginService loginService;
//
//
//    @RequestMapping(value = "/login")
//    public Result login(String username, String password) throws Exception {
//        //调用userLoginService实现登录
//        String grant_type="password";
//        AuthToken authToken = userLoginService.login(username, password, clientId, clientSecret, grant_type);
//        if (authToken!=null){
//            return  new Result(true, StatusCode.OK,"登录成功",authToken);
//        }
//        return new Result(false,StatusCode.ERROR,"登录失败");
//    }
//
////
////
////
////    //
////    @ApiOperation(value = "用户登录")
////    @PostMapping("/login")
////    public User login(@RequestBody UserLogin user, HttpServletRequest request, HttpServletResponse response) {
////        String username = user.getUsername();
////        Integer invalidUserCount = (Integer) redisUtil.hget(RedisKeyConstants.loginCountValid, username);
////
////        // 输错密码超过3次，进行验证码验证
////        if (Objects.nonNull(invalidUserCount) && invalidUserCount >= 3) {
////            String captchaResultKey = CaptchaConstant.CAPTCHA_RESULT + request.getSession().getId();
////            String verifyResult = (String) redisUtil.get(captchaResultKey);
////            if (StringUtils.isEmpty(verifyResult)) {
////                throw new CaptchaUnAuthException();
////            }
////            redisUtil.del(captchaResultKey);
////        }
////
////        // 调用实际登录接口
////        User userLogin = loginService.login(username, user.getPassword(), response);
////        redisUtil.hdel(RedisKeyConstants.loginCountValid, username);
////        return userLogin;
////    }
//
//
//
//
//
////
////    @ApiOperation(value = "用户登出")
////    @GetMapping("/logout")
////    public void logout(@RequestHeader("Authorization") String token) {
////        loginService.logout(token);
////    }
////
////    @ApiOperation(value = "token认证",notes = "网关调用")
////    @GetMapping("/token/{token}")
////    public User check(@PathVariable String token,@RequestParam(required = false) Boolean refreshExpiredTime,@RequestParam("type")String type){
////        return loginService.check(token,refreshExpiredTime,type);
////    }
////
////
////    @GetMapping("/removeAppIdToken")
////    @ApiOperation(value = "移除AppId登录token", notes = "系统内部调用")
////    @ApiIgnore
////    @PreAuthorize("hasPermission(null,'/login/removeAppIdToken')")
////    public void removeAppIdToken(@RequestParam("appIds") List<String> appIds) {
////        loginService.removeAppIdToken(appIds);
////    }
////
//}
