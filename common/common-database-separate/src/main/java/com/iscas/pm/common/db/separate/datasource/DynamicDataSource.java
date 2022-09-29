package com.iscas.pm.common.db.separate.datasource;

import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
import com.iscas.pm.common.db.separate.config.DatasourceFactory;
import lombok.extern.slf4j.Slf4j;
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
        String dataSourceName = DataSourceHolder.getDB().dataSourceName;
        String databaseName = DataSourceHolder.getDB().databaseName;
        if (!dataSourceMap.containsKey(dataSourceName)) {
            this.addDataSourceByName(dataSourceName,databaseName);
        }

        log.info(" 切换数据库到:" + dataSourceName);
        return dataSourceName;
    }

    public void addDataSourceByName(String dataSourceName,String databaseName) {
        addDataSource(dataSourceName, datasourceFactory.createDataSource(dataSourceName, databaseName));
    }

    public void addDataSource(String datasourceName, DataSource dataSource) {
        dataSourceMap.put(datasourceName, dataSource);
        this.afterPropertiesSet();
    }

    public void deleteDataSourceByName(String datasourceName) {
        dataSourceMap.remove(datasourceName);
    }
}
