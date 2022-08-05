package com.iscas.pm.common.core.model;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/7/19
 * @Description： 用户状态枚举类
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum UserStatusEnum {
    NORMAL("NORMAL","正常"),
    CANCEL("CANCEL","已注销");

    private final String code;
    private final String value;

    UserStatusEnum(String code,String value) {
        this.code = code;
        this.value = value;
    }
}
