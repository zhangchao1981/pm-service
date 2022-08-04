package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;

/**
 * 开发任务表
 * @TableName dev_task
 */
@TableName(value ="dev_task")
@Data
public class DevTask implements Serializable {


    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "任务名称")
    private String name;


    @ApiModelProperty(value = "责任人")
    private String worker;


    @ApiModelProperty(value = "任务状态")
    private String status;


    @ApiModelProperty(value = "计划工时")
    private Double scheduleHour;


    @ApiModelProperty(value = "发生工时")
    private Double happendHour;


    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startDate;


    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endDate;

    @JsonIgnore
    @ApiModelProperty(value = "开发需求id")
    private Integer requireId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}