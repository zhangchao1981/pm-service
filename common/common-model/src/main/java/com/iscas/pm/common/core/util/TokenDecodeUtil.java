package com.iscas.pm.common.core.util;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.iscas.pm.common.core.config.ResourceServerConfig;
import com.iscas.pm.common.core.model.UserDetailInfo;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.jwt.Jwt;
import org.springframework.security.jwt.JwtHelper;
import org.springframework.security.jwt.crypto.sign.RsaVerifier;
import org.springframework.security.oauth2.provider.authentication.OAuth2AuthenticationDetails;
import org.springframework.stereotype.Component;

import java.util.Map;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 解析token信息
 */
@Component
public class TokenDecodeUtil {
    // 获取令牌
    public String getToken() {
        OAuth2AuthenticationDetails authentication = (OAuth2AuthenticationDetails) SecurityContextHolder.getContext().getAuthentication().getDetails();
        String tokenValue = authentication.getTokenValue();
        return tokenValue;
    }

    /**
     * 解析token，获取当前的登录的用户的用户信息
     * @return
     */
    public UserDetailInfo getUserInfo() {
        //1.获取令牌
        String token = getToken();
        //2.获取公钥
        String pubKey = ResourceServerConfig.getPubKey();
        //3.解析token
        Jwt jwt = JwtHelper.decodeAndVerify(token, new RsaVerifier(pubKey));
        //4.返回解析后内容
        UserDetailInfo userDetailInfo = JSON.parseObject(jwt.getClaims(), UserDetailInfo.class);

        return userDetailInfo;
    }


}
