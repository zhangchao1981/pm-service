package com.iscas.pm.api.model.test.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/8/12
 * @Description： bug出现频率
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum BugProbabilityEnum {
    CERTAIN("CERTAIN", "必然出现"),
    RANDOM("RANDOM", "偶尔出现");

    private final String code;
    private final String value;

    BugProbabilityEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
