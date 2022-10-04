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
import javax.validation.constraints.Size;


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
    @Size(max = 50,message = "分类方长度不能大于50")
    private String category;

    @ApiModelProperty(value = "服务器或系统地址",required = true)
    @NotBlank(message = "服务器或系统地址不能为空")
    @Size(max = 100,message = "服务器或系统地址长度不能大于100")
    private String serverAddress;

    @ApiModelProperty(value = "用户名")
    @NotBlank(message = "用户名不能为空")
    @Size(max = 25,message = "用户名长度不能大于25")
    private String username;

    @ApiModelProperty(value = "密码")
    @Size(max = 25,message = "密码长度不能大于25")
    //数据库需修改
    private String password;

    @ApiModelProperty(value = "说明")
    @Size(max = 255,message = "说明长度不能大于255")
    private String description;



    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}