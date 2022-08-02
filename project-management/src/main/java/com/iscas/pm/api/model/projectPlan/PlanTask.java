package com.iscas.pm.api.model.projectPlan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iscas.pm.api.model.doc.Directory;
import com.iscas.pm.common.core.util.validation.CheckTimeInterval;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotNull;


@Accessors(chain = true)
@ApiModel(value = "项目计划", description = "项目计划管理，对应project_plan表")
@CheckTimeInterval(beginTime = {"startDate"},endTime = {"endDate"},message = "计划开始日期不能大于结束日期")
@TableName(value ="project_plan")
@Data
public class PlanTask implements Serializable {

    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "任务id")
    private Integer id;

    @ApiModelProperty(value = "WBS编号")
    private String wbs;

    @ApiModelProperty(value = "父任务id，根节点的父任务id为0")
    private Integer parentId;

    @ApiModelProperty(value = "关联文档路径")
    private String docPath;

    @ApiModelProperty(value = "是否为里程碑节点")
    @TableField("is_milestone")
    private Boolean milestone;

    @ApiModelProperty(value = "是否为基线节点")
    @TableField("is_baseline")
    private Boolean baseline;

    @ApiModelProperty(value = "任务名称")
    private String name;

   @ApiModelProperty(value = "责任人")
    private String worker;

    @ApiModelProperty(value = "人数")
    private Integer personCount;

    @ApiModelProperty(value = "工期(天)")
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
    private Float scheduleHour;

    @ApiModelProperty(value = "任务状态")
    private TaskStatusEnum status;

    @ApiModelProperty(value = "项目完成进度")
    private Double progressRate;

    @ApiModelProperty(value = "实际开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualStartDate;

    @ApiModelProperty(value = "实际结束日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualEndDate;

    @ApiModelProperty(value = "任务排序编号")
    private Integer position;

    @TableField(exist = false)
    @ApiModelProperty(value = "子任务")
    private List<PlanTask> children;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}