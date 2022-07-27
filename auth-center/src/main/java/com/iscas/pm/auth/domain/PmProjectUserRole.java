package com.iscas.pm.auth.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * 
 * @TableName pm_project_user_role
 */
@TableName(value ="pm_project_user_role")
@Data
public class PmProjectUserRole implements Serializable {

    @ApiModelProperty(value = "id")

    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "用户id")
    private Integer userId;


    @ApiModelProperty(value = "角色id")
    private Integer roleId;


    @ApiModelProperty(value = "项目id")
    private String projectId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}