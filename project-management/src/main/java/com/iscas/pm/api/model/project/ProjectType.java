package com.iscas.pm.api.model.project;

import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 项目类型枚举类
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProjectType {
    INTERNAL("INTERNAL","内部项目"),
    RESEARCH("RESEARCH","科研项目"),
    MODEL("MODEL","型号项目");

    private final String code;
    private final String value;

    ProjectType(String code,String value) {
        this.code = code;
        this.value = value;
    }
}
