package com.iscas.pm.api.model.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description：
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum SecretLevel {
    NO_SECRET("NO_SECRET","非密"),
    INTERNAL("INTERNAL","内部"),
    SECRET("SECRET","秘密");

    private final String code;
    private final String value;

    SecretLevel(String value,String code) {
        this.code = code;
        this.value = value;
    }
}
