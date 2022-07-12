package com.doc;

import org.mybatis.spring.annotation.MapperScan;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.transaction.annotation.EnableTransactionManagement;

/**
 * created By lichang on
 */
@SpringBootApplication
@EnableTransactionManagement //开启事务支持
@MapperScan("com.doc.mapper")
//@EnableDataBaseSeparate
public class DocApplication { public static void main(String[] args) {
        SpringApplication.run(DocApplication.class,args);
    }

}
