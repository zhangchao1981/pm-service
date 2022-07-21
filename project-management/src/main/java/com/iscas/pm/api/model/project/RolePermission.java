package com.iscas.pm.api.model.project;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import lombok.Data;

/**
 * 
 * @TableName pm_role_permission
 */
@TableName(value ="pm_role_permission")
@Data
public class RolePermission implements Serializable {
    /**
     * id
     */
    @TableId
    private Integer id;

    /**
     * 角色id
     */
    private Integer roleId;

    /**
     * 权限id
     */
    private String permissionId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}