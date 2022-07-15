package com.iscas.pm.common.db.separate.mybatis;

import org.mybatis.spring.boot.autoconfigure.ConfigurationCustomizer;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class MybatisConfig {
    @Bean
    public ConfigurationCustomizer configurationCustomizer() {
        return configuration -> {
            configuration.setMapUnderscoreToCamelCase(true);//设置驼峰命名规则
        };
    }
}
