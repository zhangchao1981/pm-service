package com.energy.common.databaseseparate.mybatis;

import org.apache.ibatis.mapping.DatabaseIdProvider;
import org.apache.ibatis.plugin.Interceptor;
import org.apache.ibatis.session.SqlSessionFactory;
import org.mybatis.spring.SqlSessionFactoryBean;
import org.mybatis.spring.SqlSessionTemplate;
import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.mybatis.spring.boot.autoconfigure.MybatisAutoConfiguration;
import org.mybatis.spring.boot.autoconfigure.MybatisProperties;
import org.springframework.beans.factory.ObjectProvider;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.boot.autoconfigure.condition.ConditionalOnBean;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.io.ResourceLoader;

import javax.sql.DataSource;
import java.util.List;

@Configuration
@ConditionalOnExpression("!'${spring.datasource.url:x}'.equals('x') && ${datasource.separator.enable:true}")
@EnableConfigurationProperties(MybatisProperties.class)
@ConditionalOnBean(DataSource.class)
@ConditionalOnClass({SqlSessionFactory.class, SqlSessionFactoryBean.class})
@MapperScanner(basePackages = "${mybatis.dynamic.basePackages}", sqlSessionFactoryRef = "sqlSessionFactory2")
public class MybatisDynamicDataSourceConfig extends MybatisAutoConfiguration {
    private MybatisProperties properties;

    public MybatisDynamicDataSourceConfig(MybatisProperties properties, ObjectProvider<Interceptor[]> interceptorsProvider, ResourceLoader resourceLoader, ObjectProvider<DatabaseIdProvider> databaseIdProvider, ObjectProvider<List<ConfigurationCustomizer>> configurationCustomizersProvider) {
        super(properties, interceptorsProvider, resourceLoader, databaseIdProvider, configurationCustomizersProvider);
        this.properties = properties;
    }

    @Bean("sqlSessionFactory2")
    @Override
    public SqlSessionFactory sqlSessionFactory(@Qualifier("dynamic") DataSource dataSource) throws Exception {
//        properties.setMapperLocations(new String[]{"classpath:com/gridsum/tac/auth/mapper/dynamic/*.xml"});
        properties.setConfiguration(null);
        SqlSessionFactory sqlSessionFactory = super.sqlSessionFactory(dataSource);
        return sqlSessionFactory;
    }

    @Bean
    public SqlSessionTemplate sqlSessionTemplate(@Qualifier("sqlSessionFactory2") SqlSessionFactory sqlSessionFactory) {
        return super.sqlSessionTemplate(sqlSessionFactory);
    }
}
