package com.user.domain;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Size;

@ApiModel
@Data
@Accessors(chain = true)
public class UserLogin {

    @ApiModelProperty(value = "用户名",required = true)
    @Size(max = 32,message = "用户名需要在0-32位之间")
    private String username;

    @ApiModelProperty(value = "密码",required = true)
    @Size(max = 32,min = 6,message = "密码长度请在6-32位之间")
    private String password;
}
