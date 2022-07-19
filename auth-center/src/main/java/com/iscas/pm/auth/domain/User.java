package com.iscas.pm.auth.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

/**
 * @author 李昶
 * @description 针对表user
 * @createDate 2022-07-06 11:17:11
 * 用户表
 * @TableName user
 */


@TableName(value ="auth_user")
@Data
@ApiModel(value = "User对象", description = "")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class User implements Serializable {
    /**
     * 用户id
     */
    @ApiModelProperty(value = "用户id")
    @TableId(value = "id", type = IdType.AUTO)
    private Long id;

    /**
     * 用户名
     */
    @ApiModelProperty(value = "用户名")
    @TableField(value = "userName")
    private String  userName;

    /**
     * 人员姓名
     */
    private String employeeName;



    /**
     * 密码，加密存储
     */
    @ApiModelProperty(value = "密码")
    @TableField(value = "password")
    private String password;


    /**
     * 人员姓名
     */
    @ApiModelProperty(value = "人员姓名")
    @TableField(value = "employeename")
    private String employeename;

    /**
     * 所属部门id
     */
    @ApiModelProperty(value = "所属部门id")
    @TableField(value = "departmentId")
    private Integer departmentId;

    /**
     * 注册手机号
     */
    @ApiModelProperty(value = "注册手机号")
    @TableField(value = "phone")
    private String phone;

    /**
     * 邮箱地址
     */
    @TableField(value = "email")
    @ApiModelProperty(value = "注册邮箱")
    private String email;


    /**
     * 是否注销，1 表示是，0 表示否
     */
    @ApiModelProperty(value = "是否注销")
    @TableField(value = "status")
    private Integer status;


    /**
     * 创建时间
     */
    @ApiModelProperty(value = "创建时间")
    @TableField(value = "createTime")
    private Date createTime;



    /**
     * 最后修改时间
     */
    private LocalDateTime updateTime;
}