package com.iscas.pm.auth.config;

import com.iscas.pm.auth.feign.UserDetailInfo;
import com.iscas.pm.auth.feign.UserCenterClient;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.oauth2.provider.ClientDetails;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.stereotype.Service;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 为认证服务提供用户信息，feign当时调用用户中心微服务获取
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ClientDetailsService clientDetailsService;
    @Autowired
    UserCenterClient userCenterClient;

    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //取出身份
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //如果身份为空说明没有认证，采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if(authentication==null){
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if(clientDetails!=null){
                //秘钥 从oauth_client_details表里获取
                String clientSecret = clientDetails.getClientSecret();
                //数据库查找方式
                return new User(username,clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }
        UserDetailInfo userDetails = userCenterClient.getUserDetails(username);
        userDetails.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(StringUtils.join(userDetails.getSystemPermissions(), ",")));
        return userDetails;
    }
}
