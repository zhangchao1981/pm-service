package com.energy.common.databaseseparate.filter;

import com.energy.common.databaseseparate.holder.DataSourceHolder;
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
    public void doFilter(ServletRequest servletRequest, ServletResponse servletResponse, FilterChain filterChain)
            throws IOException, ServletException {
        HttpServletRequest request = (HttpServletRequest) servletRequest;
        // 安全检查，对给予外部权限获取的变量字符，添加字符范围检查
        String databaseName = CleanPathUtil.cleanString(request.getParameter(DATA_BASE_FIELD));//从http parameter属性里找
        if (databaseName==null){
            databaseName = CleanPathUtil.cleanString(request.getHeader(DATA_BASE_FIELD));       //从header里找
        }
//        if (databaseName==null){
//            Cookie[] cookies = request.getCookies();
//            if(cookies!=null){
//                for(Cookie cookie:cookies){
//                    if(cookie.getName().equals(DATA_BASE_FIELD)){
//                        databaseName = cookie.getValue();
//                        break;
//                    }
//                }
//            }
//        }
        if(databaseName == null){
            databaseName = DataSourceHolder.DEFAULT_DATASOURCE;   //找不到就用默认的
        }
        DataSourceHolder.setDB(databaseName);
        filterChain.doFilter(servletRequest, servletResponse);
    }

}
