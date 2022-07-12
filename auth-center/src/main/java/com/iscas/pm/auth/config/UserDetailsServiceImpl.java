package com.iscas.pm.auth.config;

import com.entity.Result;
import com.entity.StatusCode;
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
import org.springframework.util.StringUtils;


/*****
 * 自定义授权认证类    放用户的其他信息
 */
//@Service
public class UserDetailsServiceImpl implements UserDetailsService {

    @Autowired
    ClientDetailsService clientDetailsService;
    @Autowired
    UserService userService;

    /****
     * 自定义授权认证
     * @param username
     * @return
     * @throws UsernameNotFoundException
     * //
     */
    @Override
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        //====================================客户端信息认证 start=======================================
        //取出身份，如果身份为空说明没有认证
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        //没有认证统一采用httpbasic认证，httpbasic中存储了client_id和client_secret，开始认证client_id和client_secret
        if (authentication == null) {//没认证，开始认证
            //查询数据库   oauth_client_details
            //调用JdbcClientDetailsService类的方法，根据用户名查询对应的信息（包括客户端信息） 这里不需要验证密码，只用验证用户名是不是在clientID表里面就行
            ClientDetails clientDetails = clientDetailsService.loadClientByClientId(username);
            if (clientDetails != null) {
                //秘钥
                String clientSecret = clientDetails.getClientSecret();
                //静态方式
//                return new User(
//                        username,     //客户端ID
//                        new BCryptPasswordEncoder().encode(clientSecret),   //客户端秘钥-->加密操作
//                        AuthorityUtils.commaSeparatedStringToAuthorityList(""));//权限
                //数据库查找方式
                return new User(username, clientSecret, AuthorityUtils.commaSeparatedStringToAuthorityList(""));
            }
        }
        //====================================客户端信息认证 end=======================================
        //====================================用户账户密码信息认证 start=======================================

        if (StringUtils.isEmpty(username)) {//这是用户前台输入的用户名，要拿来和数据库里查到的对比
            return null;                    //首先判断前台是否输入用户名了
        }
        //从数据库加载查询用户信息
        Result<com.iscas.pm.auth.domain.User> userResult = new Result<com.iscas.pm.auth.domain.User>(true, StatusCode.OK, "查询成功", userService.loadUserByUsername(username));
        if (userResult == null || userResult.getData() == null) {//有可能没有数据
            return null;
        }
        //这里加载的pwd是用于验证的，也就是数据库存的用户密码，如果用户登录login输入的密码不是这个，就会报错
        String pwd = userResult.getData().getPassword();//从数据库中查到的密码
        String permissions = "user,vip,admin";//这里应该写成从数据库里获取，但是由于表中没存，所以就简化了

        UserJwt userDetails = new UserJwt(username, pwd, AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
        //====================================用户账户密码信息认证 end=======================================
        //userDetails.setComy(songsi);
        return userDetails;
    }

}
