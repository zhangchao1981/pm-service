package com.iscas.pm.api.model.dev;

import io.swagger.annotations.ApiModelProperty;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author by  lichang
 * @date 2022/10/17.
 */
public class DataRequirementInfo {


    @ApiModelProperty(value = "数据名称",required = true)
    @NotBlank(message = "数据名称不能为空")
    private String name;

    @ApiModelProperty(value = "默认值",required = true)
    @NotBlank(message = "默认值不能为空")
    private String defaultValue;

    @ApiModelProperty(value = "是否可编辑，true可编辑",required = true)
    private Boolean canEdite;

    @ApiModelProperty(value = "范围枚举",required = true)
    @NotBlank(message = "范围枚举不能为空")
    private String rangeEnumeration;

    @ApiModelProperty(value = "备注，可填可不填",required = true)
    private String note;
}
