package com.iscas.pm.api.model.dev;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import lombok.Getter;

/**
 * @author by  lichang
 * @date 2022/8/4.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
@ApiModel("需求优先级")
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


}
