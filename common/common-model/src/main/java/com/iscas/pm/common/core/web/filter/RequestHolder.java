package com.iscas.pm.common.core.web.filter;

import com.iscas.pm.common.core.model.UserDetailInfo;

/**
 * 将用户信息存放到ThreadLocal中
 */
public class RequestHolder {

    private final static ThreadLocal<UserDetailInfo> requestHolder = new ThreadLocal<>();

    public static void add(UserDetailInfo userDetailInfo) {
        requestHolder.set(userDetailInfo);
    }

    public static UserDetailInfo getUserInfo() {
        return requestHolder.get();
    }

    public static void remove() {
        requestHolder.remove();
    }
}