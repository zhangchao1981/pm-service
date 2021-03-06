package com.iscas.pm.auth.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * 
 * @TableName auth_role_permission
 */
@TableName(value ="auth_role_permission")
@Data
public class AuthRolePermission implements Serializable {
    /**
     * id
     */
    @TableId(value = "id")
    private Integer id;

    /**
     * 角色id
     */
    @TableField(value = "role_id")
    private Integer role_id;

    /**
     * 权限id
     */
    @TableField(value = "permission_id")
    private String permission_id;

    /**
     * 项目id
     */
    @TableField(value = "project_id")
    private String project_id;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}