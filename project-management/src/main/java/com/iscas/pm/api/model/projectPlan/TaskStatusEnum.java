package com.iscas.pm.api.model.projectPlan;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 计划任务枚举类
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum TaskStatusEnum {
    UN_START("UN_START","未开始"),
    RUNNING("RUNNING","进行中"),
    FINISHED("FINISHED","已完成"),
    DELAYED_FINISH("DELAYED_FINISH","延迟完成"),
    DELAYED("DELAYED","已延迟");

    @EnumValue
    private final String code;
    private final String value;

    TaskStatusEnum(String code, String value) {
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
