package com.iscas.pm.api.config;

import org.flywaydb.core.Flyway;
import org.springframework.beans.factory.config.ConfigurableBeanFactory;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Scope;

@Configuration
public class FlywayConfig {

    @Bean
    @Scope(ConfigurableBeanFactory.SCOPE_PROTOTYPE)
    public Flyway initFlyway() {
        return Flyway.configure()
                .baselineOnMigrate(true)
                .locations("sql")
                .load();
    }
}
