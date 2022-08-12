package com.iscas.pm.api.model.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/8/12
 * @Description： 缺陷注入阶段枚举类
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum BugInjectStageEnum {
    REQUIREMENT("REQUIREMENT", "需求阶段"),
    PRODUCT_DESIGN("PRODUCT_DESIGN", "产品设计阶段"),
    DEV_DESIGN("DEV_DESIGN", "开发设计阶段"),
    FRONT("FRONT", "编码阶段（前端）"),
    BACKEND("BACKEND", "编码阶段（后端）"),
    DEPLOY("DEPLOY", "部署阶段"),
    OTHER("OTHER", "其他");

    private final String code;
    private final String value;

    BugInjectStageEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
