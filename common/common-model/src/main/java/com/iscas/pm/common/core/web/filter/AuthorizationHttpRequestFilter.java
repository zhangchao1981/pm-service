package com.iscas.pm.common.core.web.filter;

import com.iscas.pm.common.core.model.AuthConstants;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

/**
 * http请求过滤器，过滤器位置至于spring security过滤器之前，用于准备用户权限信息
 */
@Component
@Slf4j
public class AuthorizationHttpRequestFilter implements Filter {

    private static final String SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    @Autowired
    private UserDetailsServiceImpl userDetailsService;
    @Autowired
    private TokenDecode tokenDecode;



    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //从header获取中获取token
        String token = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);

        //token无效，跳过该过滤器
//        if (StringUtils.isBlank(token) || token.split("_").length != 2) {
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }

//        //feign内部调用或超级管理员调用接口,跳过获取权限步骤
//        if (token.split("_")[1].equals("1")) {
//            UserDetails user = userDetailsService.loadUserByUsername("1", null);
//            setUserDetailsToSession(user, request);
//            filterChain.doFilter(servletRequest, servletResponse);
//            return;
//        }

        // 安全检查，对给予外部权限获取的变量字符，添加字符范围检查
//        Map<String, String> userInfo = tokenDecode.getUserInfo();
//        String userId = userInfo.get("userId");

        //从redis中去除当前项目id
        String projectId = "kkkkkk";

        //获取用户权限信息并存入上下文中
        UserDetails user = userDetailsService.loadUserByUsername("1", projectId);
        setUserDetailsToSession(user, request);

        if (!request.getRequestURI().contains("/role/getPermissionsByRoleIds"))
            log.info("访问:{}", request.getRequestURI());

        filterChain.doFilter(servletRequest, servletResponse);
    }



    private void setUserDetailsToSession(UserDetails user, HttpServletRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getAuthorities(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(SECURITY_CONTEXT, SecurityContextHolder.getContext());
    }
}
