package com.iscas.pm.api.model.test.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author by  lichang
 * @date 2022/8/10.
 */
@Data
public class TestExecuteQueryParam {

    @ApiModelProperty(value = "当前页")
    @NotNull(message = "显示页面的页码不能为空")
    @Min(value = 1, message = "当前页码不能小于1")
    private Integer pageNum;

    @ApiModelProperty(value = "每页显示记录条数")
    @NotNull(message = "每页显示的条数不能为空")
    @Min(value = 1, message = "每页显示的条数不能小于1")
    private Integer pageSize;


    @ApiModelProperty(value = "测试计划id")
    private Integer planId;
}
