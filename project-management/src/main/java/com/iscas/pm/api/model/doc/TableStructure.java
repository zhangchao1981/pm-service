package com.iscas.pm.api.model.doc;

import lombok.Data;

/**
 * @author by  lichang
 * @date 2022/9/29.
 */
@Data
public class TableStructure {

    public String  field;

    public String  comment;

    public String type;

    public String collaction;

    public String Null;

    public String key;

    public String Default;

    public String extra;

    public String privileges;

}
