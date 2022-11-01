package com.iscas.pm.auth.model;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

@TableName(value ="auth_role")
@Data
public class Role implements Serializable {

    @ApiModelProperty(value = "角色id")
    @TableId(value="id", type= IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "角色名称",required = true)
    @NotBlank(message = "角色名称不能为空")
    @Size(max = 20,message = "角色名称长度不能大于20")
    private String name;

    @ApiModelProperty(value = "角色描述")
    @Size(max = 255,message = "角色描述长度不能大于255")
    private String description;

    @ApiModelProperty(value = "角色类型",required = true)
    private RoleTypeEnum type;

    @JsonIgnore
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty(value = "最后修改时间")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}