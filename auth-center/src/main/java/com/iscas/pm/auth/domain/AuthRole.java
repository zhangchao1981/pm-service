package com.iscas.pm.auth.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 
 * @TableName auth_role
 */
@TableName(value ="auth_role")
@Data
public class AuthRole implements Serializable {


    @ApiModelProperty(value = "角色id")
    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "角色名称")
    @TableField(value = "name")
    private String name;


    @ApiModelProperty(value = "角色描述")
    @TableField(value = "description")
    private String description;


    @ApiModelProperty(value = "创建时间")
    @TableField(value = "create_time")
    private LocalDateTime create_time;


    @ApiModelProperty(value = "最后修改时间")
    @TableField(value = "update_time")
    private LocalDateTime update_time;


    @ApiModelProperty(value = "角色类型")
    @TableField(value = "type")
    private RoleTypeEnum type;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}