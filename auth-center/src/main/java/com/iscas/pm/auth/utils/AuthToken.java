package com.iscas.pm.auth.utils;

import java.io.Serializable;

/****
 * @Date:2022/7/14 14:52
 * @Description:用户令牌封装
 *****/
public class AuthToken implements Serializable{

    //令牌信息
    String access_token;
    //刷新token(refresh_token)
    String refresh_token;
    //jwt短令牌
    String jti;

    public String getAccess_token() {
        return access_token;
    }

    public void setAccess_token(String access_token) {
        this.access_token = access_token;
    }

    public String getRefresh_token() {
        return refresh_token;
    }

    public void setRefresh_token(String refresh_token) {
        this.refresh_token = refresh_token;
    }

    public String getJti() {
        return jti;
    }

    public void setJti(String jti) {
        this.jti = jti;
    }
}