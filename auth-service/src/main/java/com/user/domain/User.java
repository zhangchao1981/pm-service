package com.user.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 用户表
 * @TableName user
 */


@TableName(value ="user")
@Data
@ApiModel(value = "User对象", description = "")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableId(value = "userId", type = IdType.AUTO)
    private Long userId;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @TableField(value = "username")
    private String username;

    /**
     * 密码，加密存储
     */
    @ApiModelProperty(value = "密码")
    @TableField(value = "password")
    private String password;

    /**
     * 注册手机号
     */
    @ApiModelProperty(value = "注册手机号")
    @TableField(value = "phone")
    private String phone;

    /**
     * 注册邮箱
     */
    @TableField(value = "email")
    @ApiModelProperty(value = "注册邮箱")
    private String email;

    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "created")
    private Date created;

    /**
     * 修改时间
     */
    @ApiModelProperty(value = "更新时间")
    @TableField(value = "updated")
    private Date updated;

    /**
     * 账号状态（1正常 0非正常）
     */
    @ApiModelProperty(value = "账号状态（1正常 0非正常）")
    @TableField(value = "status")
    private String status;

}