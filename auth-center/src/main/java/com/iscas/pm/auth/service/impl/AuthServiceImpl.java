package com.iscas.pm.auth.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iscas.pm.auth.service.AuthService;
import com.iscas.pm.auth.model.AuthToken;
import com.iscas.pm.common.core.util.RedisUtil;
import com.iscas.pm.common.core.web.exception.AuthenticateException;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.stereotype.Service;
import org.springframework.util.Base64Utils;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.DefaultResponseErrorHandler;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;

/**
 * @author 李昶
 * @Date: 20122/7/14 16:42
 * @Description: 调用oauth请求token、注销token
 */
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    //客户端ID
    @Value("${auth.clientId}")
    private String clientId;
    //客户端秘钥
    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Autowired
    private LoadBalancerClient loadBalancerClient;
    @Autowired
    private RestTemplate restTemplate;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public AuthToken login(String username, String password) {
        //申请令牌
        AuthToken authToken = applyToken(username, password, clientId, clientSecret);
        if (authToken == null || StringUtils.isBlank(authToken.getAccess_token())) {
            throw new RuntimeException("申请令牌失败");
        }
        return authToken;
    }

    @Override
    public boolean logout(String token) {
        //如果token是以bear 开头的，去掉开头
        if (token.startsWith("bearer")) {
            token = StringUtils.substring(token, 7, token.length());
        }
        redisUtil.hdel(RequestHolder.getUserInfo().getId().toString(), token);
        return true;
    }

    /****
     * oauth中获取token令牌
     * @param username:用户名
     * @param password：用户密码
     * @param clientId：配置文件中的客户端ID
     * @param clientSecret：配置文件中的客户端秘钥
     * @return
     */
    private AuthToken applyToken(String username, String password, String clientId, String clientSecret) {
        //注册中心获取认证服务的实例
        ServiceInstance serviceInstance = loadBalancerClient.choose("auth-server");
        if (serviceInstance == null) {
            throw new RuntimeException("申请令牌时报错，找不到auth-center服务");
        }

        //获取令牌的url
        String path = serviceInstance.getUri().toString() + "/oauth/token";
        //定义body
        MultiValueMap<String, String> formData = new LinkedMultiValueMap<>();
        //授权方式
        formData.add("grant_type", "password");
        //账号
        formData.add("username", username);
        //密码
        formData.add("password", password);
        //定义头
        MultiValueMap<String, String> header = new LinkedMultiValueMap<>();
        header.add("Authorization", httpBasic64(clientId, clientSecret));

        //配置restTemplate调用异常处理
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() == 401) {
                    throw new AuthenticateException("用户名密码错误！");
                }
                if (response.getRawStatusCode() == 400) {
                    String message = new String(super.getResponseBody(response), "UTF-8");
                    log.error("获取token令牌报错，错误信息：{}", message);
                    if (StringUtils.isNotBlank(message)) {
                        JSONObject res = JSONObject.parseObject(message);
                        throw new AuthenticateException(res.get("error_description").toString());
                    }
                    throw new AuthenticateException("用户名密码错误!");
                }
                super.handleError(response);
            }
        });

        //http请求spring security的申请令牌接口
        ResponseEntity<AuthToken> mapResponseEntity = restTemplate.exchange(path, HttpMethod.POST, new HttpEntity<>(formData, header), AuthToken.class);

        //获取响应数据
        AuthToken authToken = mapResponseEntity.getBody();
        if (authToken == null || StringUtils.isBlank(authToken.getAccess_token()) || StringUtils.isBlank(authToken.getRefresh_token()) || StringUtils.isBlank(authToken.getJti())) {
            log.error("创建令牌失败！错误原因：" + JSON.toJSONString(mapResponseEntity.getBody()));
            throw new RuntimeException("创建令牌失败！");
        }

        return authToken;
    }

    /***
     * base64编码
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String httpBasic64(String clientId, String clientSecret) {
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId + ":" + clientSecret;
        //进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic " + new String(encode);
    }
}
