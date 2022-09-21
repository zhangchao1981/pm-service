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
    DevModular("模块"),DevRequirement("开发需求"),DevTask("开发任务");
    private  String type;
    DevTypeEnum(String type) {
        this.type = type;
    }
}
