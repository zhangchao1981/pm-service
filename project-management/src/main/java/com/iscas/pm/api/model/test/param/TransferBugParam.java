package com.iscas.pm.api.model.test.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

/**
 * @Author： zhangchao
 * @Date： 2022/8/12
 * @Description：
 */
@Data
@ApiModel("转办缺陷请求参数")
public class TransferBugParam {
    @ApiModelProperty(value = "缺陷id",required = true)
    @NotNull(message = "缺陷id不能为空")
    private Integer bugId;

    @ApiModelProperty(value = "转办人姓名",required = true)
    @NotBlank(message = "转办人姓名不能为空")
    private String transferName;

    @ApiModelProperty(value = "转办人id",required = true)
    @NotBlank(message = "转办人id不能为空")
    private String transferId;

    @ApiModelProperty(value = "转办说明")
    private String explain;
}
