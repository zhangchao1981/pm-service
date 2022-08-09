package com.iscas.pm.api.model.dev;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/8/9
 * @Description：
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum RequirementSourceEnum {
    CUSTOMER("CUSTOMER", "客户"),
    PRODUCT_MANAGER("PRODUCT_MANAGER", "产品经理"),
    TECHNICAL_MANAGER("TECHNICAL_MANAGER", "技术经理"),
    DEC_LEADER("DEC_LEADER", "部门领导");

    private final String code;
    private final String value;

    RequirementSourceEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
