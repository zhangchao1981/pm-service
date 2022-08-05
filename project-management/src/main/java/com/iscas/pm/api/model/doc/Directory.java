package com.iscas.pm.api.model.doc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.List;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;


@Accessors(chain = true)
@ApiModel(value = "项目文档目录", description = "项目文档目录信息，对应项目库doc_directory表")
@TableName(value ="doc_directory")
@Data
public class Directory implements Serializable {

    @ApiModelProperty(value = "目录id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank
    @ApiModelProperty(value = "目录名称")
    private String name;


    @ApiModelProperty(value = "父节点id")
    @NotNull
    private Integer parentId;


    @TableField(exist = false)
    @ApiModelProperty(value = "子节点")
    private List<Directory>   children;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}