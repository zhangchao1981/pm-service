package com.iscas.pm.auth.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

@TableName(value ="auth_permission")
@Data
@ApiModel(value = "权限实体类")
@Accessors(chain = true)
public class Permission implements Serializable {
    
    @ApiModelProperty(value = "权限id")
    private String id;

    @ApiModelProperty(value = "权限名称",required = true)
    @NotBlank(message = "权限名称不能为空")
    private String name;

    @ApiModelProperty(value = "权限描述")
    private String description;

    @ApiModelProperty(value = "权限所属模块",required = true)
    @NotBlank(message = "权限所属模块不能为空")
    private String module;

    @JsonIgnore
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty(value = "最后修改时间")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}