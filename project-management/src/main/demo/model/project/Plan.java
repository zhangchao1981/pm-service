package com.demo.model.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iscas.pm.common.core.util.validation.CheckTimeInterval;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import java.io.Serializable;
import java.time.LocalDateTime;


@Accessors(chain = true)
@ApiModel(value = "项目计划", description = "项目计划基本信息，对应demo库plan表")
@CheckTimeInterval(beginTime = {"startDate"},endTime = {"endDate"},message = "计划开始日期不能大于结束日期")
@TableName(value ="plan")
@Data
public class Plan implements Serializable {
    /**
     * id
     */
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "WBS编号")
    private String wbs;

    @ApiModelProperty(value = "关联文档路径")
    private String docPath;

    @ApiModelProperty(value = "是否为里程碑节点，\"1\":\"是\"；\"0\":\"否\"")
    private Integer isMilestone;

    @ApiModelProperty(value = "是否为基线节点，\"1\":\"是\"；\"0\":\"否\"")
    private Integer isBaseline;



    @ApiModelProperty(value = "任务名称")
    private String name;



   @ApiModelProperty(value = "责任人")
    private String woker;

    @ApiModelProperty(value = "人数")
    private Integer personCount;

    @ApiModelProperty(value = "工期(天)")
    private Integer workingDays;

    @ApiModelProperty(value = "开始日期")
    private LocalDateTime startDate;

    @ApiModelProperty(value = "截止日期")
    private LocalDateTime endDate;


    @ApiModelProperty(value = "计划工时")
    private Object scheduleHour;

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