package com.iscas.pm.gateway.exception;

import org.springframework.boot.autoconfigure.web.ErrorProperties;
import org.springframework.boot.autoconfigure.web.ResourceProperties;
import org.springframework.boot.autoconfigure.web.reactive.error.DefaultErrorWebExceptionHandler;
import org.springframework.boot.web.reactive.error.ErrorAttributes;
import org.springframework.context.ApplicationContext;
import org.springframework.http.HttpStatus;
import org.springframework.web.reactive.function.server.*;
import org.springframework.web.server.ResponseStatusException;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 网关自定义异常处理
 */
public class CustomExceptionHandler extends DefaultErrorWebExceptionHandler {

    public CustomExceptionHandler(ErrorAttributes errorAttributes,
                                  ResourceProperties resourceProperties,
                                  ErrorProperties errorProperties,
                                  ApplicationContext applicationContext) {
        super(errorAttributes, resourceProperties, errorProperties, applicationContext);
    }

    /**
     * 封装异常信息
     */
    @Override
    protected Map<String, Object> getErrorAttributes(ServerRequest request, boolean includeStackTrace) {
        int code = 500;

        Throwable error = super.getError(request);
        if (error instanceof ResponseStatusException) {
            ResponseStatusException ex = (ResponseStatusException) error;
            code = ex.getStatus().value();
        } else if (error instanceof AuthenticateException) {
            AuthenticateException ex = (AuthenticateException) error;
            code = ex.getCode();
        }
        return response(code, error.getMessage() == null ? error.getCause().getMessage() : error.getMessage());
    }

    /**
     * 指定响应处理方法为JSON处理的方法
     */
    @Override
    protected RouterFunction<ServerResponse> getRoutingFunction(ErrorAttributes errorAttributes) {
        return RouterFunctions.route(RequestPredicates.all(), this::renderErrorResponse);
    }

    /**
     * 根据code获取对应的HttpStatus
     */
    @Override
    protected HttpStatus getHttpStatus(Map<String, Object> errorAttributes) {
        int statusCode = (int) errorAttributes.get("code");
        return HttpStatus.valueOf(statusCode);
    }

    /**
     * 按自定义结构构建带返回的异常信息
     *
     * @param status       状态码
     * @param errorMessage 异常信息
     * @return  异常信息
     */
    public static Map<String, Object> response(int status, String errorMessage) {
        Map<String, Object> map = new HashMap<>();
        map.put("code", status);
        map.put("message", errorMessage);
        map.put("data", null);
        return map;
    }

}