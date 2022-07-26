package com.iscas.pm.auth.config;

import com.iscas.pm.auth.feign.UserDetailInfo;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.oauth2.provider.token.DefaultUserAuthenticationConverter;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 自定义token内容，目前没有配置定制化内容
 */
@Component
public class CustomUserAuthenticationConverter extends DefaultUserAuthenticationConverter {

    /**
     * 自定义要存到token里的信息
     * @param authentication 默认的认证信息
     * @return 自定义添加到token中的信息
     */
    @Override
    public Map<String, ?> convertUserAuthentication(Authentication authentication) {
        HashMap response = new HashMap();
        UserDetailInfo userDetailInfo=(UserDetailInfo)authentication.getPrincipal();
        response.put("name",userDetailInfo.getUsername());
        response.put("id", userDetailInfo.getUserId());
        response.put("projectPermissions",userDetailInfo.getProjectPermissions());

        //把系统权限列表放进token里
//        response.put(userDetailInfo.get)
        if (authentication.getAuthorities() != null && !authentication.getAuthorities().isEmpty()) {
            response.put("authorities", AuthorityUtils.authorityListToSet(authentication.getAuthorities()));
        }
        return response;
    }
    

}
