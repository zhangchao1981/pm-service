package com.iscas.pm.api.model.dev;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author by  lichang
 * @date 2022/8/4.
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RequireStatusEnum {
    DESIGN("DESIGN", "设计中"),
    DEPLOYED("DEPLOYED", "已发布"),
    DEVELOPING("DEVELOPING", "开发中"),
    FINISHED("FINISHED","已完成"),
    DELAYED_FINISH("DELAYED_FINISH","延迟完成"),
    DELAYED("DELAYED", "已延迟");

    private final String code;
    private final String value;

    RequireStatusEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
