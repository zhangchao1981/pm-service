package com.iscas.pm.api.model.dev;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author by  lichang
 * @date 2022/8/16.
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum InterfaceTypeEnum {
    EXTERNAL_INTERFACE("EXTERNAL_INTERFACE","外部接口"),
    INTERNAL_INTERFACE("INTERNAL_INTERFACE","内部接口");

    private final String code;
    private final String value;

    InterfaceTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
