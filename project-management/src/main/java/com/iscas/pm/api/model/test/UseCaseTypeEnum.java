package com.iscas.pm.api.model.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * 测试用例类型
 * @author by  lichang
 * @date 2022/8/10.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum UseCaseTypeEnum {

    FUNCTION("FUNCTION", "功能测试"),
    PERFORMANCE("PERFORMANCE", "性能测试"),
    DEPLOYMENT("DEPLOYMENT", "安装部署");

    private final String code;
    private final String value;

    UseCaseTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
