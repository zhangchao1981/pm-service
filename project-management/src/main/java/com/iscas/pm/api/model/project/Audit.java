package com.iscas.pm.api.model.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

/**
 * @author by  lichang
 * @date 2022/7/21.
 */
@Data
public class Audit {
    @ApiModelProperty(value = "项目编号")
    @NotBlank(message = "项目编号不能为空")
    @Size(max = 10)
    private String id;

    @ApiModelProperty(value = "审核结果")
    @NotBlank(message = "请选择是否通过")
    @Size(max = 25)
    private ProjectStatusEnum statusEnum;

    @ApiModelProperty(value = "审核意见")
    private String auditcontext;


}
