package com.iscas.pm.common.db.separate.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.jta.atomikos.AtomikosDataSourceBean;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

import javax.sql.DataSource;
import java.util.Properties;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 数据源工厂类，通过druid创建数据源
 */
@Component
@Slf4j
public class DatasourceFactory {

    @Autowired
    private Environment environment;

    public DataSource createDataSource(String uniqName, String databaseName){
        AtomikosDataSourceBean dataSourceBean = new AtomikosDataSourceBean();
        dataSourceBean.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        dataSourceBean.setUniqueResourceName(uniqName);
        dataSourceBean.setPoolSize(5);

        ///读取配置文件配置信息
        Properties prop = build("spring.datasource.");

        //取得连接前先测试是否可用
        dataSourceBean.setTestQuery("select 1");
        if(databaseName != null){
            String url = replaceDBFromURL(prop.getProperty("url"), databaseName);
            prop.setProperty("url", url);
        }
        dataSourceBean.setXaProperties(prop);

        log.info("创建新的数据源，地址= {}", prop.getProperty("url"));
        return dataSourceBean;
    }


    public DataSource createDataSource(String name,String url,String databaseName, String userName,String password, String driverClassName){
        AtomikosDataSourceBean dataSourceBean = new AtomikosDataSourceBean();
        dataSourceBean.setXaDataSourceClassName("com.alibaba.druid.pool.xa.DruidXADataSource");
        dataSourceBean.setUniqueResourceName(name);
        dataSourceBean.setPoolSize(5);
        ///读取配置文件配置信息
        Properties prop = build(url,userName,password,driverClassName);
        //取得连接前先测试是否可用
        dataSourceBean.setTestQuery("select 1");
        if(databaseName != null){
            prop.setProperty("url", url);
        }
        dataSourceBean.setXaProperties(prop);
        log.info("创建新的数据源，地址= {}",url );
        return dataSourceBean;
    }




    private static String replaceDBFromURL(String url, String dbName){
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
//        prop.put("initialSize", environment.getProperty(prefix + "initialSize", Integer.class));
//        prop.put("maxActive", environment.getProperty(prefix + "maxActive", Integer.class));
//        prop.put("minIdle", environment.getProperty(prefix + "minIdle", Integer.class));
//        prop.put("maxWait", environment.getProperty(prefix + "maxWait", Integer.class));
//        prop.put("poolPreparedStatements", environment.getProperty(prefix + "poolPreparedStatements", Boolean.class, false));
//        prop.put("maxPoolPreparedStatementPerConnectionSize",
//                 environment.getProperty(prefix + "maxPoolPreparedStatementPerConnectionSize", Integer.class, 20));
//        prop.put("validationQuery", environment.getProperty(prefix + "validationQuery"));
//        prop.put("validationQueryTimeout", environment.getProperty(prefix + "validationQueryTimeout", Integer.class));
//        prop.put("testOnBorrow", environment.getProperty(prefix + "testOnBorrow", Boolean.class));
//        prop.put("testOnReturn", environment.getProperty(prefix + "testOnReturn", Boolean.class));
//        prop.put("testWhileIdle", environment.getProperty(prefix + "testWhileIdle", Boolean.class));
        return prop;
    }
    private Properties build(String url,String userName,String password, String driverClassName ) {
        Properties prop = new Properties();
        prop.put("url", url);
        prop.put("username", userName);
        prop.put("password", password);
        prop.put("driverClassName", driverClassName);
        return prop;
    }



}
