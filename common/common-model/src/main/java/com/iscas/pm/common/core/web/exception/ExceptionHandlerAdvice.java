package com.iscas.pm.common.core.web.exception;

import com.iscas.pm.common.core.web.response.BaseResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.dao.DataAccessException;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.validation.FieldError;
import org.springframework.validation.ObjectError;
import org.springframework.validation.beanvalidation.MethodValidationPostProcessor;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import javax.validation.ValidationException;
import java.sql.SQLException;
import java.util.Objects;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 全局配置控制器（controller），校验+异常处理
 */
@Slf4j
@RestControllerAdvice
public class ExceptionHandlerAdvice {
    /**
     * 使用校验bean的方式校验RequestParam,需要声明该类
     */
    @Bean
    public MethodValidationPostProcessor myMethodValidationPostProcessor() {
        return new MethodValidationPostProcessor();
    }

    /**
     * 其他异常处理
     */
    @ExceptionHandler(value = {Exception.class})
    public BaseResponse handleAllException(Exception ex) {
        BaseResponse response = new BaseResponse();
        response.setCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
        response.setMessage(ex.getMessage()==null ? ex.toString() : ex.getMessage());
        return response;
    }


    /**
     * spring security禁止访问异常处理
     */
    @ExceptionHandler(value = {AccessDeniedException.class})
    @ResponseStatus(HttpStatus.FORBIDDEN)
    public BaseResponse handleAccessDeniedException(AccessDeniedException ex) {
        BaseResponse response = new BaseResponse();
        response.setCode(HttpStatus.FORBIDDEN.value());
        response.setMessage("您无权访问该资源，请联系系统管理员或项目负责人分配权限！");
        return response;
    }

    /**
     * SpringMvc校验bean失败
     */
    @ExceptionHandler
    public BaseResponse badRequestException(MethodArgumentNotValidException exception) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        final FieldError fieldError = exception.getBindingResult().getFieldError();
        if(fieldError==null){
            final ObjectError globalError = exception.getBindingResult().getGlobalError();
            response.setMessage(globalError.getDefaultMessage());
        }else{
            response.setMessage(Objects.requireNonNull(fieldError).getDefaultMessage());
        }
        return response;
    }

    /**
     * SpringMvc校验RequestParam失败
     */
    @ExceptionHandler
    public BaseResponse badRequestException(ValidationException exception) {
        BaseResponse<Object> response = new BaseResponse<>();
        response.setCode(HttpStatus.BAD_REQUEST.value());
        response.setMessage(exception.getMessage());
        return response;
    }


    /**
     * 用户认证异常处理
     */
    @ExceptionHandler(value = {AuthenticateException.class})
    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    public BaseResponse handleAuthenticateException(AuthenticateException ex) {
        BaseResponse response = new BaseResponse();
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage());
        return response;
    }

    /**
     * 请求参数不合法异常处理
     */
    @ExceptionHandler(value = {IllegalArgumentException.class})
    public BaseResponse handleIllegalArgumentException(IllegalArgumentException ex) {
        BaseResponse response = new BaseResponse();
        response.setCode(400);
        response.setMessage(ex.getMessage());
        return response;
    }

    /**
     * 鉴权异常的处理
     */
    @ExceptionHandler(value = {AuthorizeException.class})
    public BaseResponse handleAuthorizeException(AuthorizeException ex) {
        BaseResponse response = new BaseResponse();
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage());
        return response;
    }

    /**
     * MySQL数据库中约束性异常处理。如：非空字段，唯一约束等
     */
    @ExceptionHandler(value = {DataIntegrityViolationException.class})
    public BaseResponse handleDataIntegrityViolationException(DataIntegrityViolationException ex) {
        log.error("不符合数据库约束性，导致异常",ex);
        BaseResponse response = new BaseResponse();
        response.setCode(500);
        response.setMessage("不符合数据库约束性，导致异常");
        return response;
    }

    /**
     * 数据库资源访问异常
     */
    @ExceptionHandler(value = {DataAccessException.class})
    public BaseResponse handleDataAccessException(DataAccessException ex) {
        log.error("数据库资源访问异常", ex);
        BaseResponse response = new BaseResponse();
        response.setCode(500);
        response.setMessage("数据库资源访问异常");
        return response;
    }

    /**
     * sql语法异常处理
     */
    @ExceptionHandler(value = {SQLException.class})
    public BaseResponse handleSQLException(SQLException ex) {
        log.error("sql语句执行出错",ex);
        BaseResponse response = new BaseResponse();
        response.setCode(ex.getErrorCode());
        response.setMessage("mysql中的sql语句执行出错");
        return response;
    }

    /**
     * 验证码校验异常处理
     */
    @ExceptionHandler(value = {CaptchaUnAuthException.class})
    public BaseResponse handleCaptchaUnAuthException(CaptchaUnAuthException ex) {
        log.error(ex.getMessage(), ex);
        BaseResponse response = new BaseResponse();
        response.setCode(ex.getCode());
        response.setMessage(ex.getMessage());
        return response;
    }

    /**
     * 自定义异常处理
     */
    @ExceptionHandler(value = {SimpleBaseException.class})
    public BaseResponse handleSimpleBaseException(SimpleBaseException ex) {
        return BaseResponse.fail(ex.getCode(),ex.getMessage());
    }
}