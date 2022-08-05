package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iscas.pm.api.model.doc.Directory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.io.Serializable;
import java.util.List;


@ApiModel(value = "项目模块", description = "项目模块表，对应project_demo库dev_modular表")
@TableName(value = "dev_modular")
@Data
public class DevModular implements Serializable {

    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "模块名称")
    private String name;


    @ApiModelProperty(value = "父节点id")
    private Integer parentId;

    @TableField(exist = false)
    @ApiModelProperty(value = "子节点")
    private List<Directory> children;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}