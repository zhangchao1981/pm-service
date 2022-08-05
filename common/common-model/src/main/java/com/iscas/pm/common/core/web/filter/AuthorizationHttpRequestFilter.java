package com.iscas.pm.common.core.web.filter;

import com.iscas.pm.common.core.model.AuthConstants;
import com.iscas.pm.common.core.model.UserDetailInfo;
import com.iscas.pm.common.core.model.UserInfo;
import com.iscas.pm.common.core.util.RedisUtil;
import com.iscas.pm.common.core.util.TokenDecodeUtil;
import com.iscas.pm.common.core.web.exception.SimpleBaseException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.annotation.Order;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： http请求过滤器，spring security过滤器之前，用于准备用户权限信息
 */
@Component
@Slf4j
@Order(1)
public class AuthorizationHttpRequestFilter implements Filter {

    private static final String SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    @Autowired
    private TokenDecodeUtil tokenDecodeUtil;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //从header获取中获取token
        String token = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);

        //放行部分请求
        if (!StringUtils.isBlank(token) && !"/oauth/token".equals(request.getRequestURI()) && !"/user/getUserDetails".equals(request.getRequestURI())) {
            //从token中解析出用户名
            String userId = tokenDecodeUtil.getUserId();

            //从redis里查询是否有相应的token，如果没有就拦截
            Object obj = redisUtil.hget(userId,StringUtils.substring(token, 7, token.length()));
            if (ObjectUtils.isEmpty(obj)) {
                throw new SimpleBaseException(401, "token已经失效，请重新登录");
            }

            //redis中取出用户信息，存入threadLocal中
            UserInfo userInfo = (UserInfo)obj;
            RequestHolder.add(userInfo);

            String currentProjectId = userInfo.getCurrentProjectId();
            request.setAttribute("databaseName",currentProjectId);

            //获取系统权限列表
            List<String> systemPermissions = userInfo.getSystemPermissions();
            systemPermissions = systemPermissions == null ? new ArrayList<>() : systemPermissions;

            //根据项目获取权限列表
            if (!"default".equals(currentProjectId)) {
                //获取当前项目的权限列表+系统角色权限列表，去重
                List<String> projectPermissions = userInfo.getProjectPermissions().get(currentProjectId);
                projectPermissions = projectPermissions == null ? new ArrayList<>() : projectPermissions;
                systemPermissions.addAll(projectPermissions);
            }

            //叠加去重权限列表
            systemPermissions = systemPermissions.stream().distinct().collect(Collectors.toList());
            String permissions = StringUtils.join(systemPermissions, ",");

            //新的权限列表存入security的上下文中
            UserDetailInfo userDetailInfo = new UserDetailInfo();
            userDetailInfo.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
            setUserDetailsToSession(userDetailInfo, request);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void setUserDetailsToSession(UserDetails user, HttpServletRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getAuthorities(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(SECURITY_CONTEXT, SecurityContextHolder.getContext());
    }
}
