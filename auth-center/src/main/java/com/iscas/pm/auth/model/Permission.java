package com.iscas.pm.auth.model;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@TableName(value = "auth_permission")
@Data
@ApiModel(value = "权限实体类")
@Accessors(chain = true)
public class Permission implements Serializable {

    @ApiModelProperty(value = "权限标识", required = true)
    @NotBlank(message = "权限标识不能为空")
    @Size(max = 100, message = "权限标识长度不能大于100")
    private String id;

    @ApiModelProperty(value = "权限名称", required = true)
    @NotBlank(message = "权限名称不能为空")
    @Size(max = 20, message = "权限名称长度不能大于20")
    private String name;

    @ApiModelProperty(value = "权限描述")
    @Size(max = 255, message = "权限描述长度不能大于255")
    private String description;

    @ApiModelProperty(value = "权限所属模块", required = true)
    @NotBlank(message = "权限所属模块不能为空")
    @Size(max = 20, message = "权限所属模块长度不能大于20")
    private String module;

    @JsonIgnore
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty(value = "最后修改时间")
    private Date updateTime;

    @JsonIgnore
    @ApiModelProperty(value = "排序")
    private Integer position;

    @JsonIgnore
    @ApiModelProperty(value = "模块排序")
    private Integer modulePosition;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}