package com.iscas.pm.api.model.project;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * 
 * @TableName pm_project_plan
 */
@TableName(value ="roject_plan")
@Data
@Accessors(chain = true)
public class ProjectPlan implements Serializable {

    @ApiModelProperty(value = "WBS编号")
    @TableId
    private String id;


    @ApiModelProperty(value = "关联文档路径")
    private String associatedDocumentPath;


    @ApiModelProperty(value = "是否为里程碑节点，\"1\":\"是\"；\"0\":\"否\"")
    private Boolean milestoneNode;


    @ApiModelProperty(value = "是否为基线节点，\"1\":\"是\"；\"0\":\"否\"")
    private Boolean baselineNode;

    @ApiModelProperty(value = "任务名称")
    private String taskName;


    @ApiModelProperty(value = "项目负责人")
    private String responsiblePerson;


    @ApiModelProperty(value = "人数")
    private Integer participantsNumber;


    @ApiModelProperty(value = "工期(天)")
    private Integer workingDays;


    @ApiModelProperty(value = "开始日期")
    private LocalDateTime startDate;


    @ApiModelProperty(value = "截止日期")
    private LocalDateTime endDate;


    @ApiModelProperty(value = "计划工时")
    private Integer scheduleTime;


    @ApiModelProperty(value = "项目状态")
    private String status;


    @ApiModelProperty(value = "项目完成进度")
    private Double progressRate;


    @ApiModelProperty(value = "实际开始日期")
    private LocalDateTime actualStartDate;


    @ApiModelProperty(value = "实际结束日期")
    private LocalDateTime actualEndDate;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}