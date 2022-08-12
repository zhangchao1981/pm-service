package com.iscas.pm.api.model.test;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author by  lichang
 * @date 2022/8/11.
 * @Describe 执行记录查询条件
 */
@Data
public class TestExecuteLogParam {
    @ApiModelProperty(value = "当前页", required = true)
    @NotNull(message = "显示页面的页码不能为空")
    @Min(value = 1, message = "当前页码不能小于1")
    private Integer pageNum;

    @ApiModelProperty(value = "每页显示记录条数", required = true)
    @NotNull(message = "每页显示的条数不能为空")
    @Min(value = 1, message = "每页显示的条数不能小于1")
    private Integer pageSize;

    @ApiModelProperty(value = "测试计划id", required = true)
    @NotNull(message = "测试计划id不能为空")
    private Integer planId;
}
