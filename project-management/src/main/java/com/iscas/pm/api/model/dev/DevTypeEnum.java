package com.iscas.pm.api.model.dev;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author by  lichang
 * @date 2022/9/19.
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DevTypeEnum {

    Modular,
    Requirement,
    Task;



}
