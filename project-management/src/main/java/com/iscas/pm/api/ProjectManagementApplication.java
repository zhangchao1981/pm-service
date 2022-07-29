package com.iscas.pm.api;

import com.iscas.pm.common.db.separate.config.EnableDataBaseSeparate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.flyway.FlywayAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;

@SpringBootApplication(exclude = {FlywayAutoConfiguration.class, DataSourceAutoConfiguration.class})
@MapperScan("com.iscas.pm.api.mapper")
@EnableDataBaseSeparate
public class ProjectManagementApplication { public static void main(String[] args) {
        SpringApplication.run(ProjectManagementApplication.class,args);
    }
}
