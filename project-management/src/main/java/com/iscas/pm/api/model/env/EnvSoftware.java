package com.iscas.pm.api.model.env;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
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
    @TableId
    private Integer id;

    @ApiModelProperty(value = "分类")
    @NotBlank(message = "分类不能为空")
    private String category;

    @ApiModelProperty(value = "软件名称")
    @NotBlank(message = "软件名称不能为空")
    private String softName;

    @ApiModelProperty(value = "版本标识")
    private String versionId;

    @ApiModelProperty(value = "来源")
    @NotBlank(message = "来源不能为空")
    private String source;

    @ApiModelProperty(value = "用途")
    @NotBlank(message = "用途不能为空")
    private String application;

    @ApiModelProperty(value = "介质")
    @NotBlank(message = "介质不能为空")
    private String medium;

    @ApiModelProperty(value = "安全保密")
    @NotBlank(message = "安全保密不能为空")
    private String security;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}