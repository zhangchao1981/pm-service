package com.iscas.pm.api.model.env;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iscas.pm.api.model.project.SecretLevelEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;

/**
 * @Author：lichang
 * @Date： 2022/8/2
 * @Description： 软件环境表
 */
@TableName(value = "env_software")
@Data
@Accessors(chain = true)
@ApiModel(value = "软件资源表", description = "软件环境需求表，对应项目环境页面子表")
public class EnvSoftware implements Serializable {

    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "分类",required = true)
    @NotBlank(message = "分类不能为空")
    @Size(max = 11,message = "分类不能大于11")
    private String category;

    @ApiModelProperty(value = "软件名称",required = true)
    @NotBlank(message = "软件名称不能为空")
    @Size(max = 32,message = "软件名称不能大于32")
    private String softName;

    @ApiModelProperty(value = "版本标识")
    @Size(max = 32,message = "版本标识不能大于32")
    private String versionId;

    @ApiModelProperty(value = "来源",required = true)
    @NotBlank(message = "来源不能为空")
    @Size(max = 32,message = "来源不能大于32")
    private String source;

    @ApiModelProperty(value = "用途",required = true)
    @NotBlank(message = "用途不能为空")
    @Size(max = 255,message = "用途长度不能大于255")
    private String application;

    @ApiModelProperty(value = "介质",required = true)
    @NotBlank(message = "介质不能为空")
    @Size(max = 11,message = "介质长度不能大于11")
    private String medium;

    @ApiModelProperty(value = "安全保密",required = true)
    @NotNull(message = "安全保密不能为空")
    private SecretLevelEnum security;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}