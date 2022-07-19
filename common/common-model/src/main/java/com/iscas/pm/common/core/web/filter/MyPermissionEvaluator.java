//package com.iscas.pm.common.core.web.interceptor;
//
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.security.access.PermissionEvaluator;
//import org.springframework.security.core.Authentication;
//import org.springframework.security.core.GrantedAuthority;
//import org.springframework.stereotype.Service;
//
//import java.io.Serializable;
//import java.util.Collection;
//
///**
// * 自定义权限验证方法
// */
//@Service
//@Slf4j
//public class MyPermissionEvaluator implements PermissionEvaluator {
//
//    @Override
//    public boolean hasPermission(Authentication authentication, Object targetDomainObject, Object permission) {
//        //超级管理员拥有所有资源的访问权限
//        if (((WebUserDetails) authentication.getPrincipal()).getUserId().equals("1"))
//            return true;
//
//        //简单的字符串比较，相同则认为有权限
//        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
//        for (GrantedAuthority authority : authorities) {
//            if (authority.getAuthority().equals(permission))
//                return true;
//        }
//        return false;
//    }
//
//    @Override
//    public boolean hasPermission(Authentication authentication, Serializable targetId, String targetType, Object permission) {
//        return false;
//    }
//}
