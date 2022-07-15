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
    private Map<Object,Object>  dataSourceMap;

    public DynamicDataSource() {
        this.dataSourceMap = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void init(){
        dataSourceMap.put("default", dataSource);
        setTargetDataSources(dataSourceMap);
        setDefaultTargetDataSource(dataSource);
    }

    @Override
    protected Object determineCurrentLookupKey() {
        String currentDB = DataSourceHolder.getDB();
        if(!dataSourceMap.containsKey(currentDB)) {
            this.addDataSourceByName(currentDB);
        }
        log.info(" 切换数据库到:"+ currentDB);
        return currentDB;
    }

    public void addDataSourceByName(String name){
        addDataSource(name, datasourceFactory.createDataSource(name, name));
    }

    public void addDataSource(String datasourceName, DataSource dataSource){
        dataSourceMap.put(datasourceName, dataSource);
        this.afterPropertiesSet();
    }

    public void deleteDataSourceByName(String datasourceName){
        dataSourceMap.remove(datasourceName);
    }
}
