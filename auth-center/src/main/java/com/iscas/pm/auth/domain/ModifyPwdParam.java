package com.iscas.pm.auth.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author： zhangchao
 * @Date： 2022/7/19
 * @Description： 修改密码请求参数
 */
@Data
@ApiModel("修改密码请求参数")
public class ModifyPwdParam {
    @ApiModelProperty(value = "旧密码")
    private String oldPassword;

    @ApiModelProperty(value = "新密码")
    private String newPassword;
}
