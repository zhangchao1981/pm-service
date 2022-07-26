package com.iscas.pm.common.core.web.filter;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONArray;
import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.core.JsonParser;
import com.iscas.pm.common.core.feign.UserCenterClient;
import com.iscas.pm.common.core.model.AuthConstants;
import com.iscas.pm.common.core.model.UserDetailInfo;
import com.iscas.pm.common.core.util.RedisUtil;
import com.iscas.pm.common.core.util.TokenDecodeUtil;
import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.apache.tomcat.util.http.parser.Authorization;
import org.apache.tomcat.util.json.JSONParser;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletRequestWrapper;
import java.io.IOException;
import java.lang.reflect.Type;
import java.util.*;
import java.util.stream.Collectors;

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
            //从redis中取出当前项目id
            Object obj = redisUtil.get(token);
            String currentProjectId = obj == null ? "default" : obj.toString();

            //切换数据源
            if (!request.getRequestURI().startsWith("/projectInfo") && !request.getRequestURI().startsWith("/auth")) {
                DataSourceHolder.setDB(currentProjectId);
            }

            //从token中解析出用户信息，放入ThreadLocal中
            Map<String, Object> userInfo = tokenDecodeUtil.getUserInfo();
            HashMap<String, Object> hashMapPermissions = JSONObject.parseObject(JSON.toJSONString(userInfo.get("projectPermissions")), HashMap.class);

            //把token里存的当前用户对应的所有项目的权限列表以hashmap形式放进threadlocal里
            RequestHolder.add(hashMapPermissions);


            //测试：
            currentProjectId = "demo";

            //用户信息中获取当前项目上的权限列表
            if (!"default".equals(currentProjectId)) {
                //存入security的上下文中
                //首先转成list  然后拼成一个string  最后传入AuthorityUtils的方法中转成authority
                List<String> permissionsList = JSONObject.parseObject(JSON.toJSONString(hashMapPermissions.get(currentProjectId)), List.class);
                //拿到系统权限 拼上去
                List<String> authorities = JSONObject.parseObject(JSON.toJSONString(userInfo.get("authorities")),List.class);

                permissionsList.addAll(authorities);
                permissionsList=permissionsList.stream().distinct().collect(Collectors.toList());
                String permissions = StringUtils.join(permissionsList, ",");

                UserDetailInfo userDetailInfo = new UserDetailInfo();
                userDetailInfo.setUserId((Integer) userInfo.get("id"));
                userDetailInfo.setUsername((String) userInfo.get("name"));
                userDetailInfo.setAuthorities(AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
                setUserDetailsToSession(userDetailInfo, request);
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
