package com.iscas.pm.auth.domain;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * 
 * @TableName pm_role_permission
 */
@TableName(value ="pm_role_permission")
@Data
public class PmRolePermission implements Serializable {


    @TableId
    private Integer id;

    /**
     *
     */
    @ApiModelProperty(value = "角色id")
    @NotBlank(message = "角色id不能为空")
    @Size(max = 100)
    private Integer roleId;


    @ApiModelProperty(value = "权限id")
    @NotBlank(message = "权限id不能为空")
    private String permissionId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}