package com.iscas.pm.auth.domain;

/**
 * @author by  lichang
 * @date 2022/7/27.
 */
public enum RoleTypeEnum {
    system("system","系统角色"),
    project("project","项目角色");

    private final String code;
    private final String value;

    RoleTypeEnum(String code,String value) {
        this.code = code;
        this.value = value;
    }
}



