package com.iscas.pm.api.model.test.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * 测试计划类型
 * @Author： zhangchao
 * @Date： 2022/10/9
 * @Description：
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum TestTypeEnum {
    UNIT("UNIT", "单元测试"),
    INTEGRATION("INTEGRATION", "集成测试"),
    QUALIFICATION("QUALIFICATION", "合格性测试"),
    SYSTEM("SYSTEM", "系统测试");

    private final String code;
    private final String value;

    TestTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
