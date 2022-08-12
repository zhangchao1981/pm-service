package com.iscas.pm.api.model.test.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/8/11
 * @Description： bug类型枚举类
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum BugTypeEnum {
    UIUE("UIUE", "UIUE问题"),
    FUNCTION("FUNCTION", "功能问题"),
    PERFORMANCE("PERFORMANCE", "性能问题"),
    SECURITY("SECURITY", "安全问题"),
    ADVISE("ADVISE", "优化建议"),
    UIUE_REDUCTION_DEGREE("UIUE_REDUCTION_DEGREE", "UIUE还原度"),
    OTHER("OTHER", "其他");

    private final String code;
    private final String value;

    BugTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
