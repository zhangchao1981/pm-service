package com.iscas.pm.api.model.projectPlan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iscas.pm.common.core.util.validation.CheckTimeInterval;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;

@Accessors(chain = true)
@ApiModel(value = "项目计划", description = "项目计划管理，对应project_plan表")
@CheckTimeInterval(beginTime = {"startDate"},endTime = {"endDate"},message = "计划开始日期不能大于结束日期")
@TableName(value ="project_plan")
@Data
public class PlanTask implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "任务id")
    private Integer id;

    @ApiModelProperty(value = "WBS编号,前端无需传参，后端自动生成")
    private String wbs;

    @ApiModelProperty(value = "父任务id，根节点的父任务id为0",required = true)
    private Integer parentId;

    @ApiModelProperty(value = "关联文档路径")
    private String docPath;

    @ApiModelProperty(value = "是否为里程碑节点",required = true)
    @TableField("is_milestone")
    private Boolean milestone;

    @ApiModelProperty(value = "是否为基线节点",required = true)
    @TableField("is_baseline")
    private Boolean baseline;

    @ApiModelProperty(value = "任务名称",required = true)
    @NotBlank(message = "任务名称不能为空")
    private String name;

   @ApiModelProperty(value = "责任人，人员姓名，多个人用逗号隔开")
    private String worker;

    @ApiModelProperty(value = "人数,前端无需传参，后端自动根据责任人计算")
    private Integer personCount;

    @ApiModelProperty(value = "工期(天),前端无需传参，后端自动根据开始日期和结束日期计算")
    private Integer workingDays;

    @ApiModelProperty(value = "开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @ApiModelProperty(value = "截止日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @ApiModelProperty(value = "计划工时")
    private Double scheduleHour;

    @ApiModelProperty(value = "发生工时，前端无需传参，后端自动生成")
    private Double happenedHour;

    @ApiModelProperty(value = "任务状态，前端无需传参")
    private TaskStatusEnum status;

    @ApiModelProperty(value = "项目完成进度，前端无需传参")
    private Integer progressRate;

    @ApiModelProperty(value = "实际开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualStartDate;

    @ApiModelProperty(value = "实际结束日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualEndDate;

    @ApiModelProperty(value = "任务排序编号，同级任务的排序，从1开始",required = true)
    private Integer position;

    @TableField(exist = false)
    @ApiModelProperty(value = "子任务")
    private List<PlanTask> children;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}