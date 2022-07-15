package com.iscas.pm.common.db.separate.filter;

import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
@ConditionalOnWebApplication
@WebFilter
public class DatasourceRequestFilter implements Filter{

    private static final String DATA_BASE_FIELD="databaseName";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 从redis中获取当前项目id
        String currentProjectId = "default";

        DataSourceHolder.setDB(currentProjectId);
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
