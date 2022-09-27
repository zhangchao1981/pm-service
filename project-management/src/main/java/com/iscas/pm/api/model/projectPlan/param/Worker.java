package com.iscas.pm.api.model.projectPlan.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @author by  lichang
 * @date 2022/9/27.
 */
@Data
public class Worker {
    @ApiModelProperty(value = "责任人姓名")
    private String workerName;

    @ApiModelProperty(value = "责任人对应userId")
    private Integer userId;
}
