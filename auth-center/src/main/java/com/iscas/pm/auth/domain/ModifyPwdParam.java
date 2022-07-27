package com.iscas.pm.auth.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotEmpty;

/**
 * @Author： zhangchao
 * @Date： 2022/7/19
 * @Description： 修改密码请求参数
 */
@Data
@ApiModel("修改密码请求参数")
public class ModifyPwdParam {
    @ApiModelProperty(value = "旧密码")
    @NotEmpty
    private String oldPassword;

    @NotEmpty
    @ApiModelProperty(value = "新密码")
    private String newPassword;
}
