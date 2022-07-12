package com.iscas.pm.auth.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author xuzhengfan on 2021/10/25
 */
@Data
@ApiModel("修改密码")
public class ModifyPwdParam {
    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "旧密码")
    private String oldPassWord;

    @ApiModelProperty(value = "新密码")
    private String newPassWord;
}
