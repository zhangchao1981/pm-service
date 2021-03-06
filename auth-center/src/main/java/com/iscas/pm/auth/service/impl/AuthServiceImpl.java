package com.iscas.pm.auth.service.impl;


import com.iscas.pm.auth.service.AuthService;
import com.iscas.pm.auth.utils.AuthToken;
import com.iscas.pm.common.core.web.exception.AuthenticateException;
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
import org.springframework.web.client.RestClientException;
import org.springframework.web.client.RestTemplate;

import java.io.IOException;
import java.nio.charset.Charset;
import java.util.Map;

/*****
 * @Date:2022/7/14 14:52
 * @Description: 授权认证login请求  service
 ****/
@Slf4j
@Service
public class AuthServiceImpl implements AuthService {
    //客户端ID
    @Value("${auth.clientId}")
    private String clientId;

    //秘钥
    @Value("${auth.clientSecret}")
    private String clientSecret;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    @Autowired
    private RestTemplate restTemplate;

    /***
     * 授权认证方法
     * @param username
     * @param password
     * @return
     */
    @Override
    public AuthToken login(String username, String password) {
        //申请令牌
        AuthToken authToken = applyToken(username, password, clientId, clientSecret);
        if (authToken == null || StringUtils.isBlank(authToken.getAccessToken())) {
            throw new RuntimeException("申请令牌失败");
        }
        return authToken;
    }


    /****
     * 认证方法
     * @param username:用户登录名字
     * @param password：用户密码
     * @param clientId：配置文件中的客户端ID
     * @param clientSecret：配置文件中的秘钥
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
        header.add("Authorization", httpbasic(clientId, clientSecret));

        //配置restTemplate调用异常处理
        restTemplate.setErrorHandler(new DefaultResponseErrorHandler() {
            @Override
            public void handleError(ClientHttpResponse response) throws IOException {
                if (response.getRawStatusCode() == 401)
                    throw new AuthenticateException("用户名密码错误！");
                if (response.getRawStatusCode() == 400){
                    String message = new String(super.getResponseBody(response), "UTF-8");
                    log.error("获取token令牌报错，错误信息：{}",message);
                    throw new AuthenticateException("用户名密码错误!");
                }
                super.handleError(response);
            }
        });

        Map map = null;
        //http请求spring security的申请令牌接口
        ResponseEntity<Map> mapResponseEntity = restTemplate.exchange(path, HttpMethod.POST, new HttpEntity<>(formData, header), Map.class);
        //获取响应数据
        map = mapResponseEntity.getBody();
        if (map == null || map.get("access_token") == null || map.get("refresh_token") == null || map.get("jti") == null) {
            //jti是jwt令牌的唯一标识作为用户身份令牌
            throw new RuntimeException("创建令牌失败！");
        }

        //将响应数据封装成AuthToken对象
        AuthToken authToken = new AuthToken();
        //访问令牌(jwt)
        String accessToken = (String) map.get("access_token");
        //刷新令牌(jwt)
        String refreshToken = (String) map.get("refresh_token");
        //jti，作为用户的身份标识
        String jwtToken = (String) map.get("jti");
        authToken.setJti(jwtToken);
        authToken.setAccessToken(accessToken);
        authToken.setRefreshToken(refreshToken);
        return authToken;
    }


    /***
     * base64编码
     * @param clientId
     * @param clientSecret
     * @return
     */
    private String httpbasic(String clientId, String clientSecret) {
        //将客户端id和客户端密码拼接，按“客户端id:客户端密码”
        String string = clientId + ":" + clientSecret;
        //进行base64编码
        byte[] encode = Base64Utils.encode(string.getBytes());
        return "Basic " + new String(encode);
    }
}
