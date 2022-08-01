package com.iscas.pm.api.model.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author by  lichang
 * @date 2022/8/1.
 * @Description： 项目密级 枚举类
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum PlanStatusEnum {
    RUNNING("RUNNING", "进行中"),
    COMPLETED("COMPLETED", "已完成"),
    NOT_START("NOT_START", "未开始"),
    DELAY_COMPLETED("DELAY_COMPLETED", "延时完成"),
    DELAYED("DELAYED", "已延时");
    private final String code;
    private final String value;

    PlanStatusEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }


}
