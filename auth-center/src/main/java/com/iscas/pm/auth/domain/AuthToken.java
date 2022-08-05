package com.iscas.pm.auth.domain;

import lombok.Data;

import java.io.Serializable;

/**
 * @Date:2022/7/14 14:52
 * @Description: 用户令牌封装
 **/
@Data
public class AuthToken implements Serializable{

    //令牌信息
    String access_token;
    //刷新token(refresh_token)
    String refresh_token;
    //jwt短令牌
    String jti;
}