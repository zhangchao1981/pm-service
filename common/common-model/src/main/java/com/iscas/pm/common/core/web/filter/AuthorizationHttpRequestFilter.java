package com.iscas.pm.common.core.web.filter;

import com.iscas.pm.common.core.feign.UserCenterClient;
import com.iscas.pm.common.core.model.AuthConstants;
import com.iscas.pm.common.core.model.UserDetailInfo;
import com.iscas.pm.common.core.util.RedisUtil;
import com.iscas.pm.common.core.util.TokenDecodeUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;
import java.util.Map;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： http请求过滤器，spring security过滤器之前，用于准备用户权限信息
 */
@Component
@Slf4j
public class AuthorizationHttpRequestFilter implements Filter {

    private static final String SECURITY_CONTEXT = "SPRING_SECURITY_CONTEXT";

    @Autowired
    private UserCenterClient userCenterClient;
    @Autowired
    private TokenDecodeUtil tokenDecodeUtil;
    @Autowired
    private RedisUtil redisUtil;

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;

        //从header获取中获取token
        String token = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);

        if (!StringUtils.isBlank(token) && !"/oauth/token".equals(request.getRequestURI()) && !"/user/getUserDetails".equals(request.getRequestURI())) {
            //从token中解析出用户信息
            Map<String, String> userInfo = tokenDecodeUtil.getUserInfo();
            String userName = userInfo.get("user_name");

            //从redis中取出当前项目id  todo redis无效token会越来越多，占用空间
            Object obj = redisUtil.get(token);
            String projectId = obj == null ? "default" : obj.toString();

            if (!"default".equals(projectId)) {
                //feign调用获取当前用户的详细信息（主要是获取在当前项目上的权限列表） todo 每次访问都查库有性能问题
                UserDetailInfo userDetails = userCenterClient.getUserDetails(userName, projectId);
                userDetails.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(userDetails.getPermissions()));

                //存入security的上下文中
                setUserDetailsToSession(userDetails, request);
            }
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

    private void setUserDetailsToSession(UserDetails user, HttpServletRequest request) {
        Authentication authentication = new UsernamePasswordAuthenticationToken(user, user.getAuthorities(), user.getAuthorities());
        SecurityContextHolder.getContext().setAuthentication(authentication);
        request.getSession().setAttribute(SECURITY_CONTEXT, SecurityContextHolder.getContext());
    }
}
