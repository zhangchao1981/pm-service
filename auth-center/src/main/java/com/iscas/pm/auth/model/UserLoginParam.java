package com.iscas.pm.auth.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
/**
 * @Author： zhangchao
 * @Date： 2022/7/19
 * @Description： 用户登录请求参数
 */
@Data
@ApiModel("用户登录请求参数")
@Accessors(chain = true)
public class UserLoginParam {

    @ApiModelProperty(value = "用户名",required = true)
    @NotBlank(message = "用户名不能为空")
    private String userName;

    @ApiModelProperty(value = "密码",required = true)
    @NotBlank(message = "密码不能为空")
    private String password;
}
