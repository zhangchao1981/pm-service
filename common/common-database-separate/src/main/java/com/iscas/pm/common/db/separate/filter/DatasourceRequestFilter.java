package com.iscas.pm.common.db.separate.filter;

import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnWebApplication;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

import javax.servlet.*;
import javax.servlet.annotation.WebFilter;
import javax.servlet.http.HttpServletRequest;
import java.io.IOException;

@Slf4j
@Component
@ConditionalOnWebApplication
@WebFilter
@Order(2)
public class DatasourceRequestFilter implements Filter{


    private static final String DATA_BASE_FIELD="databaseName";

    @Override
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain) throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;


//        //切换数据源之前先判断是否存在数据库
//        //  SELECT * FROM information_schema.SCHEMATA where SCHEMA_NAME= ${databaseName};
//        AtomikosDataSourceBean dataSourceBean = new AtomikosDataSourceBean();
//        dataSourceBean.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
//        dataSourceBean.setTestQuery("SELECT * FROM information_schema.SCHEMATA where SCHEMA_NAME= "+databaseName);



        //切换数据源
        String databaseName = (String)request.getAttribute(DATA_BASE_FIELD);
        if (request.getRequestURI().startsWith("/projectInfo")) {
            DataSourceHolder.setDB("default");
        } else {
            DataSourceHolder.setDB(databaseName);
        }
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
