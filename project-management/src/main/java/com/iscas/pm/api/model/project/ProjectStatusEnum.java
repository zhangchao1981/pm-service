package com.iscas.pm.api.model.project;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonValue;

/**
 * @author by  lichang
 * @date 2022/7/20.
 * @Description： 项目状态枚举类
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum ProjectStatusEnum {
    RUNNING("RUNNING", "进行中"),
    CHECK("CHECK", "待审核"),
    UN_PASS("UN_PASS", "未通过"),
    CLOSED("CLOSED", "已关闭");

    @EnumValue
    private final String code;

    private final String value;

    ProjectStatusEnum(String code, String value) {
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
