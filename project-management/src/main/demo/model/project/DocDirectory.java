package com.demo.model.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;


@Accessors(chain = true)
@ApiModel(value = "项目文档目录", description = "项目文档目录信息，对应demo库doc_directory表")
@TableName(value ="doc_directory")
@Data
public class DocDirectory implements Serializable {

    @ApiModelProperty(value = "目录id")
    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "目录名称")
    private String name;


    @ApiModelProperty(value = "父节点id")
    private Integer parentId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}