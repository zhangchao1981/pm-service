package com.iscas.pm.auth.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.tomcat.jni.Directory;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

/**
 * 部门表
 * @TableName auth_department
 */
@TableName(value ="auth_department")
@Data
public class Department implements Serializable {

    @ApiModelProperty(value = "部门id")

    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "部门名称不能为空")
    @ApiModelProperty(value = "部门名称",required = true)
    private String name;


    @NotNull(message = "父节点id不能为空")
    @ApiModelProperty(value = "父节点id",required = true)
    @NotNull
    private Integer parentId;


    @TableField(exist = false)
    @ApiModelProperty(value = "子节点", notes = "")
    private List<Directory> children;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}