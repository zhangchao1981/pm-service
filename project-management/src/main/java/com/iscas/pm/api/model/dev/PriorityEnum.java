package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;
import lombok.Getter;

/**
 * @author by  lichang
 * @date 2022/8/4.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PriorityEnum {

    MAX("MAX", "高"),
    MID("MID", "中"),
    MIN("MIN", "低");

    private final String code;
    private final String value;

    PriorityEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }


    public String getCode() {
        return code;
    }

    public String getValue() {
        return value;
    }

}
