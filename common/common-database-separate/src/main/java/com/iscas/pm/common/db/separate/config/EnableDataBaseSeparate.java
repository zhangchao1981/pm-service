package com.iscas.pm.common.db.separate.config;

import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)
@Retention(RetentionPolicy.RUNTIME)
@Import(DBSeparateAutoScan.class)//引导加载我们需要操作的类
@Documented
@Inherited
public @interface EnableDataBaseSeparate {
}
