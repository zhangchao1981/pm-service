package com.iscas.pm.api.model.doc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author by  lichang
 * @date 2022/9/27.
 */
@Getter
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
public enum DateBaseType {
    MYSQL,ORACLE
}
