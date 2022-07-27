package com.iscas.pm.auth.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

@TableName(value ="auth_user_role")
@Data
public class AuthUserRole implements Serializable {

    @ApiModelProperty(value = "id")
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

}