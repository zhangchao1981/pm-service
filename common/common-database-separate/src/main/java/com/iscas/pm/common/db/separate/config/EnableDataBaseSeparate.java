package com.iscas.pm.common.db.separate.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 切库包的扫描注解
 */
@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DBSeparateAutoScan.class)//引导加载我们需要操作的类
@Documented
@Inherited
public @interface EnableDataBaseSeparate {
}
