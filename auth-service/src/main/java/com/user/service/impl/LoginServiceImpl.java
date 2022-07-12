package com.user.service.impl;


import com.user.service.LoginService;
import com.user.utils.AuthToken;
import com.util.RedisUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.client.ServiceInstance;
import org.springframework.cloud.client.loadbalancer.LoadBalancerClient;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.io.UnsupportedEncodingException;
import java.util.Base64;
import java.util.Map;

@Slf4j
@Service
public class LoginServiceImpl implements LoginService {


    @Autowired
    private RedisUtil redisUtil;

    @Autowired
    private LoadBalancerClient loadBalancerClient;

    //实现请求发送
    @Autowired
    private RestTemplate restTemplate;

    @Override
    public AuthToken login(String username, String password, String clientID, String clientSecret, String grant_type) throws UnsupportedEncodingException {
        //获取指定服务的注册数据
        //1.定义url (申请令牌的url)
        //参数 : 微服务的名称spring.appplication指定的名称
        ServiceInstance choose = loadBalancerClient.choose("user");
        String url =choose.getUri().toString()+"/oauth/token";
        //        String url = "http://localhost:9001/oauth/token";
        //请求提交的数据封装
        MultiValueMap<String, String> parameterMap = new LinkedMultiValueMap();
        parameterMap.add("username", username);
        parameterMap.add("password", password);
        parameterMap.add("grant_type", grant_type);
        //请求头封装
        MultiValueMap<String, String> headerMap = new LinkedMultiValueMap<>();
        headerMap.add("Authorization","Basic "+ Base64.getEncoder().encodeToString(new String(clientID+":"+clientSecret).getBytes()));
        //HttpEntity->创建该对象  封装了请求头和请求体
        HttpEntity<MultiValueMap> httpEntity = new HttpEntity<MultiValueMap>(parameterMap,headerMap);

        //4.模拟浏览器 发送POST 请求 携带 头 和请求体 到认证服务器
        /**
         * 1.请求地址
         * 2.提交方式
         * 3.requestEntity:请求提交的数据信息封装 请求题|请求头
         * 4.responsType:返回数据需要转换的类型
         */
        ResponseEntity<Map> response = restTemplate.exchange(url, HttpMethod.POST, httpEntity, Map.class);
        Map<String, String> map = response.getBody();
        //将令牌信息转换成AuthToken对象
        AuthToken authToken = new AuthToken();
        authToken.setAccessToken(map.get("access_token"));
        authToken.setRefreshToken(map.get("refresh_token"));
        authToken.setJti(map.get("jti"));
        return authToken;
    }

}
