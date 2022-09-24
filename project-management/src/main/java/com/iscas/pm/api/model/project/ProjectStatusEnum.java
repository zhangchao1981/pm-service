package com.iscas.pm.api.model.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author by  lichang
 * @date 2022/7/20.
 * @Description： 项目状态枚举类
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum ProjectStatusEnum {
    RUNNING("RUNNING", "进行中"),
    CHECK("CHECK", "待审核"),
    UN_PASS("UN_PASS", "未通过"),
    CLOSED("CLOSED", "已关闭");

    private final String code;
    private final String value;


    ProjectStatusEnum(String code, String value) {

        this.code = code;
        this.value = value;
    }
    @Override
    public String toString() {
        return code;
    }
}
