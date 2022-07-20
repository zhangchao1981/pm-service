package com.iscas.pm.auth.config;

import com.iscas.pm.auth.service.AuthRolePermissionService;
import com.iscas.pm.auth.service.UserService;

import com.iscas.pm.auth.utils.UserJwt;
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

import javax.validation.constraints.NotEmpty;
import java.util.List;

/*****
 * 自定义授权认证类
 */
@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ClientDetailsService clientDetailsService;
    @Autowired
    UserService userService;
    @Autowired
    AuthRolePermissionService authRolePermissionService;
    /****
     * 自定义授权认证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     */
    @Override
    public UserDetails loadUserByUsername(@NotEmpty String username) throws UsernameNotFoundException {
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if(authentication==null){
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if(clientDetails!=null){
                //秘钥   从oauth_client_details表里获取
                String clientSecret = clientDetails.getClientSecret();
                //静态方式
                //return new User(username,new BCryptPasswordEncoder().encode(clientSecret), AuthorityUtils.commaSeparatedStringToAuthorityList(""));
                //数据库查找方式
                return new User(username,clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }
        com.iscas.pm.auth.domain.User user = userService.loadUserByUsername(username);
        if (user == null ) {//有可能没有数据
            return null;
        }
        //这里加载的pwd是用于验证的，也就是数据库存的用户密码，如果用户登录login输入的密码不是这个，就会报错
        String pwd = user.getPassword();//从数据库中查到的密码
        //获取系统角色下对应的权限列表 user_role
        List<String> permissionsList = authRolePermissionService.getPermissionsByUserId(user.getId());
        String permissions = "";//这里应该写成从数据库里获取，但是由于我们的表中没存，所以就简化了

        for (String permission : permissionsList) {
            permissions+=permission+",";
        }
        permissions = permissions.substring(0, permissions.length() - 1);
        //
        UserJwt userDetails = new UserJwt(username, pwd, AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
        //userDetails.setComy(songsi);
        return userDetails;
    }


}
