package com.energy.common.databaseseparate.configuration;


import org.springframework.context.annotation.Import;

import java.lang.annotation.*;

@Target(ElementType.TYPE)  //作用范围是类、接口、枚举、注解
@Retention(RetentionPolicy.RUNTIME)
@Import(AutoConfiguration.class)//引导加载我们需要操作的类
@Documented//被@documented注解修饰的注解(例如@B)，在修饰别的类时，在生成文档的时候会显示@B
@Inherited
public @interface EnableDataBaseSeparate {
}
