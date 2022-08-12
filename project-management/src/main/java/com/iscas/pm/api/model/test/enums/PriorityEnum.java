package com.iscas.pm.api.model.test.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * 测试用例等级
 * @author by  lichang
 * @date 2022/8/10.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum PriorityEnum {
    FIRST("FIRST", "一级"),
    SECOND("SECOND", "二级"),
    THIRD("THIRD", "三级"),
    FOURTH("FOURTH", "四级");

    private final String code;
    private final String value;

    PriorityEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
