package com.iscas.pm.api.model.dev;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iscas.pm.api.model.projectPlan.TaskStatusEnum;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author by  lichang
 * @date 2022/10/9.
 */
@Data
@Accessors(chain = true)
public class DevRequirementQueryParam {
    @ApiModelProperty(value = "查询类型", notes = "DevRequirement或者", required = true)
    private String queryType;

    @ApiModelProperty(value = "要查询内容的名称或编号", required = true)
    private String nameOrCode;

    @ApiModelProperty(value = "责任人,传参是人员姓名，支持多选", required = true)
    private List<String> workerList;

    @ApiModelProperty(value = "开发需求状态 ", required = true)
    private RequireStatusEnum requireStatus;

    @ApiModelProperty(value = "开发任务状态 ", required = true)
    private TaskStatusEnum taskStatus;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间", required = true)
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间", required = true)
    private Date endDate;

}
