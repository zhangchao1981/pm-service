package com.iscas.pm.api.model.test.enums;

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
    DOCUMENT("DOCUMENT", "文档问题"),
    PRODUCT_DESIGN("PRODUCT_DESIGN", "产品设计问题"),
    DEV_DESIGN("DEV_DESIGN", "开发设计问题"),
    FRONT("FRONT", "编码问题（前端）"),
    BACKEND("BACKEND", "编码问题（后端）"),
    DEPLOY("DEPLOY", "部署问题"),
    OTHER("OTHER", "其他");

    private final String code;
    private final String value;

    BugInjectStageEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
