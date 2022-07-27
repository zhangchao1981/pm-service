package com.iscas.pm.auth.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName auth_user_role
 */
@TableName(value ="auth_user_role")
@Data
public class AuthUserRole implements Serializable {

    @ApiModelProperty(value = "id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "角色id")
    @TableField(value = "role_id")
    private Integer roleId;


    @ApiModelProperty(value = "用户id")
    @TableField(value = "user_id")
    private Integer userId;

}