package com.iscas.pm.auth.domain;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author by  lichang
 * @date 2022/7/27.
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
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



