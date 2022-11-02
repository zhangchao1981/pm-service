package com.iscas.pm.api.model.doc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;

import com.iscas.pm.common.core.util.validation.CheckTimeInterval;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Accessors(chain = true)
@ApiModel(value = "引用文档表", description = "引用文档基本信息，对应project_demo库doc_reference表")
@TableName(value ="doc_reference")
@Data
public class ReferenceDoc implements Serializable {

    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "外键：模板id",required = true)
    private Integer templateId;

    @NotBlank(message = "引用文档名称不能为空")
    @ApiModelProperty(value = "文档名称",required = true)
    @Size(max = 25,message = "文档名称长度不能大于25")
    private String name;

    @ApiModelProperty(value = "标识")
    @Size(max = 25,message = "标识长度不能大于25")
    private String mark;

    @ApiModelProperty(value = "版本号")
    @Size(max = 10,message = "标识长度不能大于10")
    private String version;

    @NotBlank(message = "引用文档来源不能为空")
    @ApiModelProperty(value = "来源")
    @Size(max = 25,message = "来源长度不能大于25")
    private String source;

    @ApiModelProperty(value = "备注")
    @Size(max = 25,message = "备注长度不能大于25")
    private String notes;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}