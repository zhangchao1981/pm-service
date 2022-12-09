package com.iscas.pm.api.model.doc.param;

import com.iscas.pm.api.model.doc.DataBaseTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author by  lichang
 * @date 2022/9/27.
 */
@Data
@Accessors(chain = true)
public class DBLinkParam {

    @ApiModelProperty(value = "数据库名", required = true)
    @NotBlank(message = "数据库名不能为空")
    private String dbName;

    @ApiModelProperty(value = "用户名", required = true)
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty(value = "密码", required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

    @ApiModelProperty(value = "数据库地址", required = true)
    @NotBlank(message = "数据库地址不能为空")
    private String dbPath;

    @ApiModelProperty(value = "数据库端口号", required = true)
    @NotBlank(message = "数据库端口号不能为空")
    private String port;

    @ApiModelProperty(value = "数据库类型", required = true)
    @NotNull(message = "数据库类型不能为空")
    private DataBaseTypeEnum dbType;

}
