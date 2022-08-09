package com.iscas.pm.api.model.dev;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/8/9
 * @Description： 需求类型枚举类
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum RequirementTypeEnum {
    FUNCTION("FUNCTION", "功能需求"),
    PERFORMANCE("PERFORMANCE", "性能需求"),
    DEPLOY("DEPLOY", "部署需求");

    private final String code;
    private final String value;

    RequirementTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
