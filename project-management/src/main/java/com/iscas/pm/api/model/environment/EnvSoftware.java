package com.iscas.pm.api.model.environment;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import lombok.Data;

import java.io.Serializable;

/**
 * @Author：lichang
 * @Date： 2022/8/2
 * @Description： 软件环境表
 */
@TableName(value ="env_software")
@Data
public class EnvSoftware implements Serializable {
    /**
     * 序号
     */
    @TableId
    private Integer id;

    /**
     * 分类
     */
    private String category;

    /**
     * 版本标识
     */
    private String versionId;

    /**
     * 来源
     */
    private String source;

    /**
     * 用途
     */
    private String application;

    /**
     * 介质
     */
    private String medium;

    /**
     * 安全保密
     */
    private String security;

    /**
     * 软件名称
     */
    private String softName;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}