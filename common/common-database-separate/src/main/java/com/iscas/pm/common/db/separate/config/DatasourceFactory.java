package com.iscas.pm.common.db.separate.config;

import com.alibaba.druid.pool.DruidDataSource;
import com.alibaba.druid.pool.DruidDataSourceFactory;
import com.iscas.pm.common.db.separate.holder.DBInfo;
import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;
import org.springframework.util.ObjectUtils;
import org.springframework.util.StringUtils;

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

    public DataSource createDataSource(String databaseName) {
        DruidDataSource druidDataSource;

        //获取数据源配置信息
        Properties prop = build(databaseName);

        try {
            druidDataSource = (DruidDataSource) DruidDataSourceFactory.createDataSource(prop);
            //自定义数据源配置重连次数
            if (!StringUtils.isEmpty(DataSourceHolder.getDB().getUrl())) {
                // 失败后重连的次数(prop中设置不起作用)
                druidDataSource.setConnectionErrorRetryAttempts(1);
                //请求失败之后中断
                druidDataSource.setBreakAfterAcquireFailure(true);
            }
        } catch (Exception e) {
            throw new RuntimeException(e);
        }

        log.info("创建新的数据源，地址= {}", prop.getProperty("url"));
        return druidDataSource;
    }

    private static String replaceDBFromURL(String url, String dbName) {
        int hostEnd = url.lastIndexOf('/');
        int propStart = url.indexOf('?');
        String props = "";
        if (propStart != -1) {
            props = url.substring(propStart);
        }
        return url.substring(0, hostEnd + 1) + dbName + props;
    }

    private Properties build(String databaseName) {
        Properties prop = new Properties();
        DBInfo dbInfo = DataSourceHolder.getDB();

        //用户自定义的数据源
        if (!ObjectUtils.isEmpty(dbInfo) && !StringUtils.isEmpty(dbInfo.getUrl())) {
            prop.put("url", dbInfo.getUrl());
            prop.put("username", dbInfo.getUserName());
            prop.put("password", dbInfo.getPassword());
            prop.put("driverClassName", dbInfo.getDriverClassName());
            prop.put("maxWait", "500");
        }
        //系统内部数据源
        else {
            String prefix = "spring.datasource.";
            String url = environment.getProperty(prefix + "url");
            prop.put("url", StringUtils.isEmpty(databaseName) ? url : replaceDBFromURL(url, databaseName));
            prop.put("username", environment.getProperty(prefix + "username"));
            prop.put("password", environment.getProperty(prefix + "password"));
            prop.put("driverClassName", environment.getProperty(prefix + "driverClassName", ""));
        }
        return prop;
    }

}
