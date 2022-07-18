package com.iscas.pm.auth.domain;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName auth_permission
 */
@TableName(value ="auth_permission")
@Data
@ApiModel(value = "User对象", description = "")
@EqualsAndHashCode(callSuper = false)
@Accessors(chain = true)
public class AuthPermission implements Serializable {
    /**
     * 资源id
     */
    @TableId(value = "id")
    private String id;

    /**
     * 资源名称
     */
    @TableField(value = "name")
    private String name;

    /**
     * 资源描述
     */
    @TableField(value = "description")
    private String description;

    /**
     * 资源所属模块
     */
    @TableField(value = "module")
    private String module;

    /**
     * 创建时间
     */
    @TableField(value = "create_time")
    private LocalDateTime createTime;

    /**
     * 最后修改时间
     */
    @TableField(value = "update_time")
    private LocalDateTime updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}