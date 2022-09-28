package com.iscas.pm.api.model.doc.param;

import com.iscas.pm.api.model.doc.DateBaseType;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author by  lichang
 * @date 2022/9/27.
 */
@Data
public class DateBaseLinkParam {

    @ApiModelProperty(value = "数据库名",required = true)
    private  String  dbName;

    @ApiModelProperty(value = "用户名",required = true)
    private  String  userName;

    @ApiModelProperty(value = "密码",required = true)
    private  String  password;

    @ApiModelProperty(value = "数据库地址",required = true)
    private  String  dbPath;

    @ApiModelProperty(value = "数据库端口号",required = true)
    private  String  port;

    @ApiModelProperty(value = "数据库驱动名",required = true)
    private  String   driverClassName;

    @ApiModelProperty(value = "数据库类型",required = true)
    private DateBaseType dbType;

}
