package com.energy.common.databaseseparate.datasource;

import com.energy.common.databaseseparate.holder.DataSourceHolder;
import com.energy.common.databaseseparate.configuration.DatasourceFactoryBean;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.jdbc.datasource.lookup.AbstractRoutingDataSource;
import org.springframework.stereotype.Component;

import javax.annotation.PostConstruct;
import javax.sql.DataSource;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

//@PropertySource({"classpath:application*.properties"})
@Component("dynamic")
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnExpression("!'${spring.datasource.url:x}'.equals('x') && ${datasource.separator.enable:true}")
@Slf4j
public class DynamicDataSource extends AbstractRoutingDataSource {

    private Map<Object,Object>  dataSourceMap;

    @Autowired
    private DatasourceFactoryBean datasourceFactoryBean;

    @Autowired
    @Qualifier("major")
    private DataSource dataSource;

    public DynamicDataSource() {
        this.dataSourceMap = new ConcurrentHashMap<>();
    }

    @PostConstruct
    public void init(){
        setTargetDataSources(dataSourceMap);
        setDefaultTargetDataSource(dataSource);
    }

    public void addDataSource(String datasourceName, DataSource dataSource){
        dataSourceMap.put(datasourceName, dataSource);
        log.debug("datasource[{}] is added.", datasourceName);
        this.afterPropertiesSet();
    }

    public void deleteDataSourceByName(String datasourceName){
        dataSourceMap.remove(datasourceName);
    }

    public void addDataSourceByname(String name){
        addDataSource(name, datasourceFactoryBean.createDataSource(name, name));
    }

    @Override
    protected Object determineCurrentLookupKey() {
        log.info(" 当前所在数据库:"+ DataSourceHolder.getDB());
        String name = DataSourceHolder.getDB();
        if(!dataSourceMap.containsKey(name)) {
            this.addDataSourceByname(name);
        }
        return DataSourceHolder.getDB();
    }
}
