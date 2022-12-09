package com.iscas.pm.api.model.test.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/8/12
 * @Description： 缺陷严重程度枚举类
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum BugSeverityEnum {

    DEADLY("DEADLY", "致命"),
    CRITICAL("CRITICAL", "严重"),
    NORMAL("NORMAL", "一般"),
    SLIGHT("SLIGHT", "优化建议");

    private final String code;
    private final String value;

    BugSeverityEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
