package com.iscas.pm.common.db.separate.datasource;

import com.iscas.pm.common.db.separate.config.DatasourceFactory;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Slf4j
@Configuration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
public class DefaultDataSource{
    @Bean("major")
    @Primary
    public DataSource dataSource(DatasourceFactory factoryBean){
        return factoryBean.createDataSource("default", null);
    }
}
