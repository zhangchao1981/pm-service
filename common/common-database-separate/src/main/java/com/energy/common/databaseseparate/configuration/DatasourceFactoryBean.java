package com.energy.common.databaseseparate.configuration;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Properties;

@Component
@Slf4j
public class DatasourceFactoryBean{

    @Autowired
    private Environment environment;

    public DataSource createDataSource(String uniqName, String databaseName){
        org.springframework.boot.jta.atomikos.AtomikosDataSourceBean dataSourceBean = new org.springframework.boot.jta.atomikos.AtomikosDataSourceBean();
        Properties prop = build("spring.datasource.");
        dataSourceBean.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        dataSourceBean.setUniqueResourceName(uniqName);
        dataSourceBean.setPoolSize(5);
        //取得连接前先测试是否可用
        dataSourceBean.setTestQuery("select 1");
        if(databaseName != null){
            String url = replaceDBfromURL(prop.getProperty("url"), databaseName);
            prop.setProperty("url", url);
        }
        log.debug("The url of datasource[{}] is {}", databaseName, prop.getProperty("url"));
        dataSourceBean.setXaProperties(prop);
        return dataSourceBean;
    }

    private static String replaceDBfromURL(String url, String dbName){
        int hostEnd = url.lastIndexOf('/');
        int propStart = url.indexOf('?');
        String props = "";
        if(propStart != -1){
            props = url.substring(propStart);
        }
        return url.substring(0, hostEnd + 1) + dbName + props;
    }

    private Properties build(String prefix) {
        Properties prop = new Properties();
        prop.put("url", environment.getProperty(prefix + "url"));
        prop.put("username", environment.getProperty(prefix + "username"));
        prop.put("password", environment.getProperty(prefix + "password"));
        prop.put("driverClassName", environment.getProperty(prefix + "driverClassName", ""));
        prop.put("initialSize", environment.getProperty(prefix + "initialSize", Integer.class));
        prop.put("maxActive", environment.getProperty(prefix + "maxActive", Integer.class));
        prop.put("minIdle", environment.getProperty(prefix + "minIdle", Integer.class));
        prop.put("maxWait", environment.getProperty(prefix + "maxWait", Integer.class));
        prop.put("poolPreparedStatements", environment.getProperty(prefix + "poolPreparedStatements", Boolean.class, false));
        prop.put("maxPoolPreparedStatementPerConnectionSize",
                 environment.getProperty(prefix + "maxPoolPreparedStatementPerConnectionSize", Integer.class, 20));
        prop.put("validationQuery", environment.getProperty(prefix + "validationQuery"));
        prop.put("validationQueryTimeout", environment.getProperty(prefix + "validationQueryTimeout", Integer.class));
        prop.put("testOnBorrow", environment.getProperty(prefix + "testOnBorrow", Boolean.class));
        prop.put("testOnReturn", environment.getProperty(prefix + "testOnReturn", Boolean.class));
        prop.put("testWhileIdle", environment.getProperty(prefix + "testWhileIdle", Boolean.class));
        return prop;
    }
}
