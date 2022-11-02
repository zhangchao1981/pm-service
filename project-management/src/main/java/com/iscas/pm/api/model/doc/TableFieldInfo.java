package com.iscas.pm.api.model.doc;

import lombok.Data;

/**
 * @author by  lichang
 * @date 2022/9/29.
 */
@Data
public class TableFieldInfo {

    public String fieldName;

    public String fieldComment;

    public String fieldType;

    public String fieldCollation;

    public String fieldNull;

    public String fieldKey;

    public String fieldDefault;

    public String fieldExtra;

    public String fieldPrivileges;

}
