package com.iscas.pm.gateway.filter;

import com.iscas.pm.common.model.AuthConstants;
import com.iscas.pm.common.model.exception.AuthenticateException;
import com.iscas.pm.common.model.BaseResponse;
import com.iscas.pm.common.model.User;
import com.iscas.pm.gateway.feign.AuthCenter;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.core.Ordered;
import org.springframework.http.server.reactive.ServerHttpRequest;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 网关过滤器，校验token是否有效
 */
@Component
@Slf4j
public class AccessGlobalFilter implements GlobalFilter, Ordered {

    @Autowired
    private AuthCenter authCenter;

    @Value("${ignorePaths}")
    private String[] ignorePaths;

    @Override
    public int getOrder() {
        return 6;
    }

    @Override
    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {

        ServerHttpRequest request = exchange.getRequest();
        String path = request.getURI().getPath();

        //跳过不需要token可直接访问的请求
        if (ignoreTokenCheck(path))
            return chain.filter(exchange);

        //获取token
        String token = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION_HEADER);
        if (StringUtils.isBlank(token))
            throw new AuthenticateException();

        //调用auth-center接口，验证token是否有效
        BaseResponse<User> response = authCenter.check(token);
        if (response == null)
            throw new AuthenticateException();
        if (response.getCode() != 200 || response.getData() == null) {
            if (StringUtils.isBlank(response.getMessage()))
                throw new AuthenticateException();
            else
                throw new AuthenticateException(response.getMessage());
        }

        //设置header信息
        User user = response.getData();
        if (StringUtils.isNotBlank(user.getCurrentProjectId())) {
            request.mutate().header("currentProjectId", user.getCurrentProjectId()).build();
        }
        if (StringUtils.isNotBlank(user.getUserId())) {
            request.mutate().header("userId", user.getUserId()).build();
        }
        if (StringUtils.isNotBlank(user.getUserName())) {
            request.mutate().header("userName", user.getUserName()).build();
        }

        return chain.filter(exchange);
    }

    private boolean ignoreTokenCheck(String path) {
        for (String item : ignorePaths) {
            if (path.contains(item) || item.contains(path)) {
                return true;
            }
        }
        return false;
    }

}
