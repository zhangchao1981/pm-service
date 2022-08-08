package com.iscas.pm.api.model.projectPlan;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 计划任务枚举类
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum TaskStatusEnum {
    UN_START("UN_START","未开始"),
    RUNNING("RUNNING","进行中"),
    FINISHED("FINISHED","已完成"),
    DELAYED_FINISH("DELAYED_FINISH","延迟完成"),
    DELAYED("DELAYED","已延迟");

    private final String code;
    private final String value;

    TaskStatusEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
