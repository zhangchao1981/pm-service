package com.iscas.pm.api.model.dev;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author by  lichang
 * @date 2022/10/17.
 * 开发接口数据描述中的数据类型
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DataTypeEnum {
    INT("INT","整型"),

    FLOAT("FLOAT","浮点型"),

    STRING("STRING","字符串类型"),

    DATE("DATE","日期类型"),

    DATETIME("DATETIME","日期时间类型"),

    ENUM("ENUM","枚举类型"),

    BOOLEAN("BOOLEAN","布尔类型"),

    OBJECT("OBJECT","对象类型"),

    ARRAYLIST("ARRAYLIST","数组类型"),

    FILE("FILE","文件类型");


    private final String code;
    private final String value;

    DataTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
