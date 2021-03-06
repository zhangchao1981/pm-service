package com.iscas.pm.auth.utils;


import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.userdetails.User;

import java.util.Collection;
/****
 * 自定义token携带信息
 *
 * @return
 */
public class UserJwt extends User {
    private String id;    //用户ID
    private String name;  //用户名字

    private String comny;//设置公司

    public UserJwt(String username, String password, Collection<? extends GrantedAuthority> authorities) {
        super(username, password, authorities);
    }

    public String getComny() {
        return comny;
    }

    public void setComny(String comny) {
        this.comny = comny;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
