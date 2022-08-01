package com.iscas.pm.api.model.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iscas.pm.common.core.util.validation.CheckTimeInterval;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;


@Accessors(chain = true)
@ApiModel(value = "项目计划", description = "项目计划基本信息，对应项目库project_plan表")
@CheckTimeInterval(beginTime = {"startDate"}, endTime = {"endDate"}, message = "计划开始日期不能大于结束日期")
@CheckTimeInterval(beginTime = {"actualStartDate"}, endTime = {"actualEndDate"}, message = "计划实际开始日期不能大于结束日期")
@TableName(value = "project_plan")
@Data
public class Plan implements Serializable {

    @ApiModelProperty(value = "项目计划编号")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "WBS编号")
    private String wbs;

    @ApiModelProperty(value = "关联文档路径")
    private String docPath;

    @NotNull
    @ApiModelProperty(value = "是否为里程碑节点，\"1\":\"是\"；\"0\":\"否\"")
    private Boolean milestone;

    @NotNull
    @ApiModelProperty(value = "是否为基线节点，\"1\":\"是\"；\"0\":\"否\"")
    private Boolean baseline;

    @NotNull(message = "任务名称不能为空")
    @ApiModelProperty(value = "任务名称")
    private String name;

    @NotNull(message = "责任人不能为空")
    @ApiModelProperty(value = "责任人")
    private String woker;

    @Min(value = 1, message = "至少参与1人")
    @ApiModelProperty(value = "人数")
    private Integer personCount;


    @Min(value = 1, message = "至少1天")
    @ApiModelProperty(value = "工期(天)")
    private Integer workingDays;

    @ApiModelProperty(value = "开始日期")
    private Date startDate;

    @ApiModelProperty(value = "截止日期")
    private Date endDate;

    @ApiModelProperty(value = "计划工时")
    private Object scheduleHour;


    @ApiModelProperty(value = "计划状态")
    private PlanStatusEnum status;

    @ApiModelProperty(value = "项目完成进度")
    private Double progressRate;

    @ApiModelProperty(value = "实际开始日期")
    private Date actualStartDate;

    @ApiModelProperty(value = "实际结束日期")
    private Date actualEndDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}