package com.iscas.pm.auth.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
/**
 * @Author： zhangchao
 * @Date： 2022/7/19
 * @Description： 用户登录VO
 */
@Data
@ApiModel("用户登录请求参数")
@Accessors(chain = true)
public class UserLoginParam {

    @ApiModelProperty(value = "用户名",required = true)
    @Size(max = 32,message = "用户名需要在0-32位之间")
    @NotBlank
    private String userName;

    @ApiModelProperty(value = "密码",required = true)
    @Size(max = 32,min = 6,message = "密码长度请在6-32位之间")
    @NotBlank
    private String password;
}
