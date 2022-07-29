package com.iscas.pm.api.model.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;


@Accessors(chain = true)
@ApiModel(value = "文档生成模板", description = "文档生成模板，对应demo库template表")
@TableName(value ="template")
@Data
public class Template implements Serializable {


    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "模板名称")
    private String name;


    @ApiModelProperty(value = "模板描述")
    private String description;


    @ApiModelProperty(value = "模板文件路径")
    private String path;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}