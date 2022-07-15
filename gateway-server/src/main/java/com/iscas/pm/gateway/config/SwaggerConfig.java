package com.iscas.pm.gateway.config;

import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Component;
import springfox.documentation.swagger.web.SwaggerResource;
import springfox.documentation.swagger.web.SwaggerResourcesProvider;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： swagger配置类，整合各微服务api接口
 */
@Component
@Primary
public class SwaggerConfig implements SwaggerResourcesProvider {

    public static final String API_URI = "/v2/api-docs";

    @Override
    public List<SwaggerResource> get() {
        List<SwaggerResource> resources = new ArrayList<>();
        resources.add(swaggerResource("认证中心", "/auth-center"));
        resources.add(swaggerResource("项目管理", "/project-management"));

        return resources;
    }

    private SwaggerResource swaggerResource(String name, String location) {
        SwaggerResource swaggerResource = new SwaggerResource();
        swaggerResource.setName(name);
        swaggerResource.setLocation(location + API_URI);
        swaggerResource.setSwaggerVersion("2.0");
        return swaggerResource;
    }

}
