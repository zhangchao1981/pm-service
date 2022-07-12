package com.iscas.pm.gateway;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.netflix.eureka.EnableEurekaClient;
import org.springframework.cloud.openfeign.EnableFeignClients;

/**
 * @Author： lichang
 * @Date： 2022/7/11
 * @Description： 微服务网关
 */
@SpringBootApplication
@EnableEurekaClient
@EnableFeignClients(basePackages = {"com.iscas.pm.gateway.feign"})
public class GatewayApplication {
    public static void main(String[] args) {
        SpringApplication.run(GatewayApplication.class,args);
    }

}
