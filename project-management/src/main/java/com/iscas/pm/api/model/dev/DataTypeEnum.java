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
//    整型（xxxint）
//    位类型(bit)
//    浮点型（float和double、real）
//    定点数（decimal,numeric）
//    日期时间类型（date,time,datetime,year）
//    字符串（char,varchar,xxxtext）
//    二进制数据（xxxBlob、xxbinary）
//    枚举（enum）
//    集合（set）
    INT("INT","整型"),
    FLOAT("FLOAT","浮点型");


    private final String code;
    private final String value;

    DataTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
