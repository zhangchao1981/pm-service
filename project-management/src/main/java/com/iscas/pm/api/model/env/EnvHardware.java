package com.iscas.pm.api.model.env;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;


/**
 * @Author：lichang
 * @Date： 2022/8/2
 * @Description： 硬件环境需求表
 */
@TableName(value ="env_hardware")
@Data
@Accessors(chain = true)
@ApiModel(value = "硬件环境需求表", description = "硬件环境需求表，对应项目环境页面子表")
public class EnvHardware implements Serializable {
    @ApiModelProperty(value = "硬件表id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "硬件名称")
    @NotBlank(message = "硬件名称不能为空")
    private String hardwareName;

    @ApiModelProperty(value = "最低配置")
    @NotBlank(message = "最低配置不能为空")
    private String minConfig;

    @ApiModelProperty(value = "数量")
    @NotNull(message = "数量不能为空")
    private Integer amount;

    @ApiModelProperty(value = "用途")
    @NotBlank(message = "用途不能为空")
    private String application;

    @ApiModelProperty(value = "使用时间")
    @NotBlank(message = "使用时间不能为空")
    private String usePeriod;

    @ApiModelProperty(value = "保密范围")
    @NotBlank(message = "安全保密不能为空")
    private String security;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}