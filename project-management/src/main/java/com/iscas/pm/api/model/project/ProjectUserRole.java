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
import javax.validation.constraints.Size;

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

    @ApiModelProperty(value = "用户id",required = true)
    @NotNull
    @Size(max = 11,message = "用户id不能大于11")
    private Integer userId;

    @TableField(exist = false)
    @ApiModelProperty(value = "人员名称",required = true)
    private String employeeName;

    @ApiModelProperty(value = "角色id",required = true)
    @NotNull
    private Integer roleId;

    @JsonIgnore
    @ApiModelProperty(value = "项目id",required = true)
    private String projectId;

    @TableField(exist = false)
    @ApiModelProperty(value = "用户名",required = true)
    private String userName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}