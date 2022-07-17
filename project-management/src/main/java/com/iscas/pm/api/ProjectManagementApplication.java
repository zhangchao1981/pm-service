package com.iscas.pm.api;

import com.iscas.pm.common.db.separate.config.EnableDataBaseSeparate;
import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

/**
 * created By lichang on
 */
@SpringBootApplication
//@EnableTransactionManagement //开启事务支持
@MapperScan("com.iscas.pm.api.mapper")
@EnableDataBaseSeparate
public class ProjectManagementApplication { public static void main(String[] args) {
        SpringApplication.run(ProjectManagementApplication.class,args);
    }

}
