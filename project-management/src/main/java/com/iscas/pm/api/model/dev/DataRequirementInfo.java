package com.iscas.pm.api.model.dev;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author by  lichang
 * @date 2022/10/17.
 */
@Data
@ApiModel(value = "数据需求中的行数据信息")
public class DataRequirementInfo {

    @ApiModelProperty(value = "数据名称",required = true)
    @NotBlank(message = "数据名称不能为空")
    private String name;

    @ApiModelProperty(value = "默认值",required = true)
    @NotBlank(message = "默认值不能为空")
    private String defaultValue;

    @ApiModelProperty(value = "是否可编辑，true可编辑",required = true)
    private Boolean canEdite;

    @ApiModelProperty(value = "是否非空，true非空",required = true)
    private Boolean notNull;

    @ApiModelProperty(value = "范围枚举",required = true)
    @NotBlank(message = "范围枚举不能为空")
    private String rangeEnumeration;

    @ApiModelProperty(value = "数据类型",required = true)
    @NotNull(message = "数据类型不能为空")
    private DataTypeEnum dataType;

    @ApiModelProperty(value = "备注，可填可不填",required = true)
    private String note;

    public String getCanEdite() {
        return canEdite?"是":"否";
    }

    public String getNotNull() {
        return notNull?"是":"否" ;
    }

    public String getDataType() {
        return dataType.getValue();
    }
}
