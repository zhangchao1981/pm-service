package com.iscas.pm.api.model.environment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDate;


/**
 * @Author：lichang
 * @Date： 2022/8/2
 * @Description： 硬件环境需求表
 */
@TableName(value ="env_hardware")
@Data
@Accessors(chain = true)
@ApiModel(value = "硬件环境需求表", description = "硬件环境需求表，对应项目资源页面子表")
public class EnvHardware implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    /**
     * 硬件名称
     */
    private String hardwareName;

    /**
     * 最低配置
     */
    private String minConfig;

    /**
     * 数量
     */
    private Integer amount;

    /**
     * 用途
     */
    private String application;

    /**
     * 使用时间
     */
    private LocalDate usePeriod;

    /**
     * 安全保密
     */
    private String security;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}