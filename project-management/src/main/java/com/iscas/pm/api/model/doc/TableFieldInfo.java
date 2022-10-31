package com.iscas.pm.api.model.doc;

import com.baomidou.mybatisplus.annotation.TableField;
import lombok.Data;
import org.apache.ibatis.annotations.ResultMap;

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
