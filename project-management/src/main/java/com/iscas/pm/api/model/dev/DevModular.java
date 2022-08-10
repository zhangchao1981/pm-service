package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iscas.pm.api.model.doc.Directory;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;


@ApiModel(value = "项目模块", description = "项目模块表，对应project_demo库dev_modular表")
@TableName(value = "dev_modular")
@Data
public class DevModular implements Serializable {

    @ApiModelProperty(value = "id",notes = "不需前端传入")
    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "模块名称",required = true)
    private String name;

    @NotNull(message = "父节点id不能为空")
    @ApiModelProperty(value = "父节点id",required = true)
    private Integer parentId;

    @TableField(exist = false)
    @ApiModelProperty(value = "子节点")
    private List<Directory> children;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}