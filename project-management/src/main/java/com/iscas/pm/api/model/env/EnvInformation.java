package com.iscas.pm.api.model.env;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


/**
 * @Author：lichang
 * @Date： 2022/8/2
 * @Description： 环境资源表
 */
@TableName(value = "env_information")
@Data
@Accessors(chain = true)
@ApiModel(value = "环境说明表", description = "环境说明表，对应项目环境页面子表")
public class EnvInformation implements Serializable {

    @ApiModelProperty(value = " 主键id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "分类",required = true)
    @NotBlank(message = "分类不能为空")
    private String category;

    @ApiModelProperty(value = "服务器或系统地址",required = true)
    @NotBlank(message = "服务器或系统地址不能为空")
    private String serverAddress;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    private String username;

    @ApiModelProperty(value = "密码")
    private String password;

    @ApiModelProperty(value = "说明")
    private String description;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}