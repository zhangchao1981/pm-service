package com.iscas.pm.common.db.separate.config;

import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.Configuration;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 添加切库组件包扫描，与EnableDataBaseSeparate注解配合使用，实现自动扫描
 */
@Configuration
@ComponentScan({"com.iscas.pm.common.db.separate"})
public class DBSeparateAutoScan {

}
