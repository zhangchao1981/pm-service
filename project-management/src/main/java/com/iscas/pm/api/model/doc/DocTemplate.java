package com.iscas.pm.api.model.doc;

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

@Accessors(chain = true)
@ApiModel(value = "文档生成模板", description = "文档生成模板，对应project_demo库doc_template表")
@TableName(value ="doc_template")
@Data
public class DocTemplate implements Serializable {

    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "模板名称不能为空")
    @ApiModelProperty(value = "模板名称")
    private String name;

    @ApiModelProperty(value = "模板描述")
    private String description;

    @ApiModelProperty(value = "模板文件路径")
    private String path;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}