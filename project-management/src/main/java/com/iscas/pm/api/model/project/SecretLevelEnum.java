package com.iscas.pm.api.model.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 项目密级 枚举类
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SecretLevelEnum {
    NO_SECRET("NO_SECRET","非密"),
    INTERNAL("INTERNAL","内部"),
    SECRET("SECRET","秘密");

    private final String value;
    private final String code;

    SecretLevelEnum(String code,String value) {
        this.code = code;
        this.value = value;
    }

    @Override
    public String toString() {
        return code;
    }
}
