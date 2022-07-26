package com.iscas.pm.common.core.web.filter;


import java.security.Permissions;
import java.util.HashMap;
import java.util.Map;

/**
 * 将用户信息存放到ThreadLocal中
 */
public class RequestHolder {

    private final static ThreadLocal<HashMap> requestHolder = new ThreadLocal<HashMap>();

    public static void add(HashMap permissions) {
        requestHolder.set(permissions);
    }

    public static HashMap getPermissions() {
        return requestHolder.get();
    }

    public static void remove() {
        requestHolder.remove();
    }
}