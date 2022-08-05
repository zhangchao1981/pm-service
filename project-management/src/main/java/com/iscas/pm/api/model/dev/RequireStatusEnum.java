package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author by  lichang
 * @date 2022/8/4.
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RequireStatusEnum {
    DESIGN("DESIGN", "需求设计"),
    DEVELOPING("DEVELOPING", "开发中");

    private final String code;
    private final String value;

    RequireStatusEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
