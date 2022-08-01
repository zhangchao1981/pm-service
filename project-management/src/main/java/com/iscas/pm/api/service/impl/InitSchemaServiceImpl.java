package com.iscas.pm.api.service.impl;

import com.iscas.pm.api.mapper.project.InitializationMapper;
import com.iscas.pm.api.service.InitSchemaService;
import com.mysql.jdbc.Driver;
import lombok.extern.slf4j.Slf4j;
import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Lookup;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.jdbc.DataSourceBuilder;
import org.springframework.jdbc.datasource.SimpleDriverDataSource;
import org.springframework.stereotype.Service;
import org.springframework.util.Assert;

import javax.sql.DataSource;

@Slf4j
@Service
public class InitSchemaServiceImpl implements InitSchemaService {
    @Autowired
    private InitializationMapper mapper;

    @Value("${spring.datasource.url}")
    private String url;
    @Value("${spring.datasource.username}")
    private String username;
    @Value("${spring.datasource.password}")
    private String password;

    private DataSource createDatasource(String databaseName){
        SimpleDriverDataSource driverDataSource = DataSourceBuilder
                .create()
                .type(SimpleDriverDataSource.class)
                .url(url)
                .username(username)
                .password(password)
                .build();
        driverDataSource.setDriverClass(Driver.class);
        driverDataSource.setCatalog(databaseName);
        return driverDataSource;
    }

    @Override
    public boolean initSchema(String databaseName) {
        DataSource dataSource = createDatasource(databaseName);
        return initSchema(databaseName, dataSource);
    }

    @Lookup
    public Flyway getFlyway() {
        return null;
    }

    @Override
    public boolean initSchema(String databaseName, DataSource dataSource) {
        Flyway flyway = getFlyway();
        Assert.notNull(dataSource, "DatabaseName[" + databaseName + "] is not initialized.");
        mapper.createDatabase(databaseName);
        flyway.setDataSource(dataSource);
        flyway.migrate();
        log.info("创建mysql数据库及数据表完成", databaseName);
        return true;
    }
}
