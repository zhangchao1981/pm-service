package com.iscas.pm.api.model.doc.data;

import com.iscas.pm.api.model.dev.DataTypeEnum;
import com.iscas.pm.api.model.dev.InterfaceDataDescription;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author by  lichang
 * @date 2022/11/2.
 */
@Data
public class DocInterfaceDataDescription {
    @ApiModelProperty(value = "数据元素名称",required = true)
    @NotBlank(message = "数据元素名称不能为空")
    @Size(max = 25,message = "数据元素名称长度不能大于25")
    private String dataName;

    @ApiModelProperty(value = "数据类型",required = true)
    private String dataType;

    @ApiModelProperty(value = "数据描述",required = true)
    @NotBlank(message = "数据描述不能为空")
    @Size(max = 100,message = "数据描述长度不能大于100")
    private String dataDescription;

    @ApiModelProperty(value = "值域范围",required = true)
    @Size(max = 500,message = "值域范围长度不能大于500")
    private String valueRange;

    @ApiModelProperty(value = "备注",required = true)
    @Size(max = 100,message = "备注长度不能大于100")
    private String note;

    public DocInterfaceDataDescription(InterfaceDataDescription dataDescription) {
        this.dataName =dataDescription.getDataName();
        this.dataType = dataDescription.getDataType().getValue();
        this.dataDescription = dataDescription.getDataDescription();
        this.valueRange = dataDescription.getValueRange();
        this.note = dataDescription.getNote();
    }
}
