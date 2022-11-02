package com.iscas.pm.api.model.doc.data;

import com.iscas.pm.api.model.dev.DataRequirementInfo;
import com.iscas.pm.api.model.dev.DataTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @author by  lichang
 * @date 2022/11/1.
 */
@Data
public class DocDataRequirementInfo {
    @ApiModelProperty(value = "序号",required = true)
    private Integer id;

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
    private String dataType;

    @ApiModelProperty(value = "备注，可填可不填",required = true)
    private String note;

    public DocDataRequirementInfo(Integer id, DataRequirementInfo dataRequirementInfo) {
        this.id = id;
        this.name =dataRequirementInfo.getName();
        this.defaultValue = dataRequirementInfo.getDefaultValue();
        this.canEdite = dataRequirementInfo.getCanEdite();
        this.notNull = dataRequirementInfo.getNotNull();
        this.rangeEnumeration = dataRequirementInfo.getRangeEnumeration();
        this.dataType = dataRequirementInfo.getDataType().getValue();
        this.note = dataRequirementInfo.getNote();
    }
}
