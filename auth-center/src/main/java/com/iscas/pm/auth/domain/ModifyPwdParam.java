package com.iscas.pm.auth.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author 66410
 * @description 针对表【user(用户表)】修改密码封装
 * @createDate 2022-07-06 11:17:11
 * @Entity com.user.domain.ModifyPwdParam
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
