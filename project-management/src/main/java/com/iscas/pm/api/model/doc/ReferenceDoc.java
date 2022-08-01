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


@Accessors(chain = true)
@ApiModel(value = "引用文档表", description = "引用文档基本信息，对应项目库reference_doc表")
@CheckTimeInterval(beginTime = {"startDate"},endTime = {"endDate"},message = "计划开始日期不能大于结束日期")

@TableName(value ="reference_doc")
@Data
public class ReferenceDoc implements Serializable {



    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "外键：模板id")
    private Integer templateId;


    @ApiModelProperty(value = "文档名称")
    private String name;


    @ApiModelProperty(value = "标识")
    private String mark;


    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "来源")
    private String source;


    @ApiModelProperty(value = "备注")
    private String notes;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}