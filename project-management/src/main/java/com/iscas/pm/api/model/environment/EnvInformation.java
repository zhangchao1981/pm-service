package com.iscas.pm.api.model.environment;


import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import lombok.Data;
import lombok.experimental.Accessors;


/**
 * @Author：lichang
 * @Date： 2022/8/2
 * @Description： 环境资源表
 */
@TableName(value ="env_information")
@Data
@Accessors(chain = true)
@ApiModel(value = "环境资源表", description = "硬件环境需求表，对应项目资源页面子表")
public class EnvInformation implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}