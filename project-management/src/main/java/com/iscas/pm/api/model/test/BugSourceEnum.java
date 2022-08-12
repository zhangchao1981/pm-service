package com.iscas.pm.api.model.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/8/12
 * @Description： 缺陷来源枚举类
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum BugSourceEnum {
    DEV_INTERNAL("DEV_INTERNAL", "研发内部"),
    INTERNAL_CUSTOMER("INTERNAL_CUSTOMER", "内部客户"),
    OPERATION("OPERATION", "运维"),
    EXTERNAL_CUSTOMER("EXTERNAL_CUSTOMER", "外部客户");

    private final String code;
    private final String value;

    BugSourceEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
