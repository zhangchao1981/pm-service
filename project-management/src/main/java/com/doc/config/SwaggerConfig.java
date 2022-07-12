package com.doc.config;




import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

import java.util.ArrayList;

/**Swagger使用的配置文件
 * created By lichang on
 */
@Configuration   //配置
@EnableSwagger2  //开启swagger2
public class SwaggerConfig {


    //配置了Swagger的Docker的Bean实例
    @Bean
    public Docket docket() {
        return new Docket(DocumentationType.SWAGGER_2)
                .apiInfo(apiInfo())
                .enable(true) //配置是否启用Swagger，如果是false，在浏览器将无法访问
                .select()// 通过.select()方法，去配置扫描接口,RequestHandlerSelectors配置如何扫描接口
                .apis(RequestHandlerSelectors.basePackage("com.doc.controller"))
                .paths(PathSelectors.any())
                .build();
    }

    //配置swagger信息 apiInfo
    private ApiInfo apiInfo() {
        //作者信息
        Contact DEFAULT_CONTACT = new Contact("李昶", "http://baidu.com", "664101181@qq.com");

        return new ApiInfo(
                "api文档",
                "api描述信息",
                "v1.0",
                "http://localhost:8089",
                DEFAULT_CONTACT,
                "Apache 2.0",
                "http://www.apache.org/licenses/LICENSE-2.0",
                new ArrayList());

    }




}