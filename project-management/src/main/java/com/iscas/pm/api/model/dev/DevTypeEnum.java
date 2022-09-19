package com.iscas.pm.api.model.dev;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author by  lichang
 * @date 2022/9/19.
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DevTypeEnum {

    DevModular("DevModular","模块"),
    DevRequirement("DevRequirement","开发需求"),
    DevTask("DevTask","开发任务");

    private final String code;
    private final String value;

    DevTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }



}
