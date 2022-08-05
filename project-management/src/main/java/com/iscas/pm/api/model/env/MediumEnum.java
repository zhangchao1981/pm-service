package com.iscas.pm.api.model.env;

import com.baomidou.mybatisplus.annotation.EnumValue;
import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author by  lichang
 * @date 2022/8/3.
 * @Describe 是否使用(待定)
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum MediumEnum {
    Disk("Disk", "光盘");


    @EnumValue
    private final String code;

    private final String value;

    MediumEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
