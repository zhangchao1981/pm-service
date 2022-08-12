package com.iscas.pm.api.model.projectPlan.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 查询条件封装
 */
@Data
public class ProjectPlanQueryParam {

    @ApiModelProperty(value = "任务名称")
    private String name;

    @ApiModelProperty(value = "任务状态")
    private String status;

    @ApiModelProperty(value = "责任人")
    private String worker;

    @ApiModelProperty(value = "父任务id，根节点的父任务id为0")
    @NotNull
    private Integer parentId;

}
