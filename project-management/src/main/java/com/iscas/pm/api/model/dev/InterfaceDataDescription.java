package com.iscas.pm.api.model.dev;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author by  lichang
 * @date 2022/10/17.
 * 开发接口数据描述
 */
@Data
public class InterfaceDataDescription {
    @ApiModelProperty(value = "数据元素名称",required = true)
    @NotBlank(message = "数据元素名称不能为空")
    @Size(max = 25,message = "数据元素名称长度不能大于25")
    private String dataName;

    @ApiModelProperty(value = "数据类型",required = true)
    private DataTypeEnum dataType;

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

}
