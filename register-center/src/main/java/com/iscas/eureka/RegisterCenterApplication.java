package com.iscas.eureka;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.server.EnableEurekaServer;

/**
 * @Author： zhangchao
 * @Date： 2022/7/11
 * @Description： Eureka注册中心
 */
@SpringBootApplication
@EnableEurekaServer
public class RegisterCenterApplication {
    public static void main(String[] args) {
        SpringApplication.run(RegisterCenterApplication.class,args);
    }
}
