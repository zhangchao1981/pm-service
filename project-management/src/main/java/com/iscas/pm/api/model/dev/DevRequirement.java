package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iscas.pm.common.core.util.validation.CheckTimeInterval;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.io.Serializable;
import java.time.LocalDateTime;


@TableName(value = "dev_requirement")
@Accessors(chain = true)
@ApiModel(value = "开发需求表", description = "开发需求表板，对应project_demo库dev_requirement表")
@CheckTimeInterval(beginTime = {"startDate"},endTime = {"endDate"},message = "项目开始日期不能大于项目结束日期")
@Data
public class DevRequirement implements Serializable {

    @ApiModelProperty(value = "需求编号  6位数")
    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "需求用例名称")
    private String name;


    @ApiModelProperty(value = "责任人")
    private String worker;


    @ApiModelProperty(value = "优先级")
    private String priority;


    @ApiModelProperty(value = "需求类型")
    private String requirementType;


    @ApiModelProperty(value = "变更需求  0: 无变更 1:有变更")
    private Boolean change;


    @ApiModelProperty(value = "状态")
    private String status;


    @ApiModelProperty(value = "开发进度")
    private Double devProgress;


    @ApiModelProperty(value = "计划工时")
    private Double scheduleHour;


    @ApiModelProperty(value = "开始时间")
    private LocalDateTime startDate;


    @ApiModelProperty(value = "结束时间")
    private LocalDateTime endDate;


    @ApiModelProperty(value = "发生工时")
    private Double happendHour;

    @JsonIgnore
    @ApiModelProperty(value = "需求来源")
    private String source;

    @JsonIgnore
    @ApiModelProperty(value = "用例说明")
    private String useCase;

    @JsonIgnore
    @ApiModelProperty(value = "模块id")
    private Integer modularId;

    @JsonIgnore
    @ApiModelProperty(value = "原型设计图路径，多个用逗号隔开")
    private String prototype;

    @JsonIgnore
    @ApiModelProperty(value = "更新时间")
    private LocalDateTime updateTime;

    @JsonIgnore
    @ApiModelProperty(value = "生成时间")
    private LocalDateTime createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}