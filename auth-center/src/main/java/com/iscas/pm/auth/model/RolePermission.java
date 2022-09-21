package com.iscas.pm.auth.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;
import java.io.Serializable;

@TableName(value ="auth_role_permission")
@Data
public class RolePermission implements Serializable {
    @ApiModelProperty(value = "id")
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "角色id")
    private Integer roleId;

    @ApiModelProperty(value = "权限id")
    @Size(max = 50,message = "权限id长度不能大于50")
    private String permissionId;

    @ApiModelProperty(value = "项目id")
    @TableField(exist = false)
    private String projectId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}