package com.energy.common.databaseseparate.datasource;

import com.energy.common.databaseseparate.configuration.DatasourceFactoryBean;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.AutoConfigureBefore;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Primary;

import javax.sql.DataSource;

@Slf4j
@Configuration
@AutoConfigureBefore(DataSourceAutoConfiguration.class)
@ConditionalOnExpression("!'${spring.datasource.url:x}'.equals('x') && ${datasource.separator.enable:true}")
public class DefaultDataSource{

    @Bean("major")
    @Primary
    public DataSource dataSource(DatasourceFactoryBean factoryBean){
        return factoryBean.createDataSource("default", null);
    }
}
