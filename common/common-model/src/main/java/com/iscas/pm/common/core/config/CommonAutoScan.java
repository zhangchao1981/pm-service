package com.iscas.pm.common.core.config;

import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 扫描common-core包
 */
@Configuration
@ComponentScan({"com.iscas.pm.common.core"})
@EnableFeignClients(basePackages = {"com.iscas.pm.common.core.feign"})
public class CommonAutoScan {

}
