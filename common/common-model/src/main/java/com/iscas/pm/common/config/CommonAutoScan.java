package com.iscas.pm.common.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 扫描common-core包
 */
@Configuration
@ComponentScan({"com.iscas.pm.common"})
public class CommonAutoScan {

}
