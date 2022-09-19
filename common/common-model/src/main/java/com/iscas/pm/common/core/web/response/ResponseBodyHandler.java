package com.iscas.pm.common.core.web.response;

import lombok.extern.slf4j.Slf4j;
import org.springframework.boot.autoconfigure.condition.ConditionalOnClass;
import org.springframework.core.MethodParameter;
import org.springframework.http.MediaType;
import org.springframework.http.converter.HttpMessageConverter;
import org.springframework.http.server.ServerHttpRequest;
import org.springframework.http.server.ServerHttpResponse;
import org.springframework.http.server.ServletServerHttpResponse;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.mvc.method.annotation.ResponseBodyAdvice;

import javax.servlet.http.HttpServletResponse;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 返回值统一处理，supports方法中过滤不需要处理的方法
 */
@Slf4j
@RestControllerAdvice(annotations = RestController.class)  //仅对注解为@RestController的类的返回值做处理
@ConditionalOnClass(ResponseBodyAdvice.class)
public class ResponseBodyHandler implements ResponseBodyAdvice<Object> {

    @Override
    public boolean supports(MethodParameter returnType, Class<? extends HttpMessageConverter<?>> converterType) {
        if (returnType.getMethod().getName().equals("getUserDetails"))
            return false;
        return true;
    }

    @Override
    public Object beforeBodyWrite(Object body, MethodParameter returnType, MediaType selectedContentType, Class<? extends HttpMessageConverter<?>> selectedConverterType, ServerHttpRequest request, ServerHttpResponse response) {

        BaseResponse baseResponse;
        HttpServletResponse httpResponse = ((ServletServerHttpResponse) response).getServletResponse();
        if (body instanceof BaseResponse) {
            baseResponse = (BaseResponse) body;
        } else {
            baseResponse = new BaseResponse();
            baseResponse.setCode(httpResponse.getStatus());
            baseResponse.setData(body);
            baseResponse.setMessage("OK");
        }
        return baseResponse;
    }
}
