package com.iscas.pm.api.model.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @author by  lichang
 * @date 2022/7/21.
 */
@Data
public class Approve {
    @ApiModelProperty(value = "项目编号", required = true)
    @NotBlank(message = "项目编号不能为空")
    @Size(max = 10)
    private String id;

    @ApiModelProperty(value = "审核通过结果", required = true)
    @NotNull(message = "审核结果不能为空")
    private Boolean pass;

    @ApiModelProperty(value = "审核意见")
    private String approveComments;


}
