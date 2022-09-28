package com.iscas.pm.common.db.separate.datasource;

import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
import com.iscas.pm.common.db.separate.config.DatasourceFactory;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 动态数据源，通过改变DataSourceHolder值切换数据源
 */
@Component("dynamic")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {
    @Autowired
    private DatasourceFactory datasourceFactory;

    @Autowired
    @Qualifier("major")
    private DataSource dataSource;

    //存放所有数据源
    private Map<Object, Object> dataSourceMap;

    public DynamicDataSource() {
        this.dataSourceMap = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void init() {
        dataSourceMap.put("default", dataSource);
        setTargetDataSources(dataSourceMap);
        setDefaultTargetDataSource(dataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String currentDB = DataSourceHolder.getDB();
        if (StringUtils.isNotBlank(DataSourceHolder.url)) {
//            this.addDataSource(DataSourceHolder.);
            this.addUniqDataSource("test",DataSourceHolder.url,DataSourceHolder.databaseName,DataSourceHolder.UserName,DataSourceHolder.password,DataSourceHolder.driverClassName);
            return "test";
        }
        if (!dataSourceMap.containsKey(currentDB)) {
            this.addDataSourceByName(currentDB);
        }

        log.info(" 切换数据库到:" + currentDB);
        return currentDB;
    }
    //入口都在determinCurrentLook

    //重写一个：


    public void addDataSourceByName(String name) {
        addDataSource(name, datasourceFactory.createDataSource(name, name));
    }


    public void addUniqDataSource(String name,String url, String databaseName, String userName, String password, String driverClassName) {
        dataSourceMap.put(name, datasourceFactory.createDataSource(name,url, databaseName, userName, password, driverClassName));
        this.afterPropertiesSet();
    }


    public void addDataSource(String datasourceName, DataSource dataSource) {
        dataSourceMap.put(datasourceName, dataSource);
        this.afterPropertiesSet();
    }

    public void deleteDataSourceByName(String datasourceName) {
        dataSourceMap.remove(datasourceName);
    }
}
