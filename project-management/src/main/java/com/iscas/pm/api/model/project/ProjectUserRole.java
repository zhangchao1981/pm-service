package com.iscas.pm.api.model.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @TableName pm_project_user_role
 * @Author：李昶
 * @Date： 2022/7/21
 * @Description： 表对应实体类
 */

@TableName(value ="pm_project_user_role")
@Data
public class ProjectUserRole implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("用户id")
    @NotNull
    private Integer userId;

    @ApiModelProperty("人员名称")
    private String employeeName;

    @ApiModelProperty("角色id")
    @NotNull
    private Integer roleId;

    @JsonIgnore
    @ApiModelProperty("项目id")
    private String projectId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}