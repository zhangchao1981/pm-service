package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;


@ApiModel(value = "开发任务", description = "开发任务表，对应project_demo库dev_requirement表")
@TableName(value = "dev_task")
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

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private Date endDate;

    @JsonIgnore
    @ApiModelProperty(value = "开发需求id")
    private Integer requireId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}