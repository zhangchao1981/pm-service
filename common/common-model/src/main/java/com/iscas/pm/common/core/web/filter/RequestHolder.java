package com.iscas.pm.common.core.web.filter;

import com.iscas.pm.common.core.model.UserInfo;

/**
 * 将用户信息存放到ThreadLocal中
 */
public class RequestHolder {

    private final static ThreadLocal<UserInfo> requestHolder = new ThreadLocal<>();

    public static void add(UserInfo userInfo) {
        requestHolder.set(userInfo);
    }

    public static UserInfo getUserInfo() {
        return requestHolder.get();
    }

    public static void remove() {
        requestHolder.remove();
    }
}