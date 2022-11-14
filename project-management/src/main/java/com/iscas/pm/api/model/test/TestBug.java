package com.iscas.pm.api.model.test;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iscas.pm.api.model.test.enums.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

/**
 * 测试缺陷表
 *
 * @TableName test_bug
 */

@Accessors(chain = true)
@ApiModel(value = "缺陷管理")
@TableName(value = "test_bug")
@Data
public class TestBug implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "缺陷编号")
    private Integer id;

    @NotBlank(message = "[缺陷标题]不能为空")
    @Size(max = 50, message = "缺陷标题长度不能超过50")
    @ApiModelProperty(value = "缺陷标题",required = true)
    private String title;

    @ApiModelProperty(value = "缺陷状态，前端无需传参")
    private BugStatusEnum status;

    @NotNull(message = "[缺陷严重程度]不能为空")
    @ApiModelProperty(value = "缺陷严重程度",required = true)
    private BugSeverityEnum severity;

    @NotNull(message = "[优先级]不能为空")
    @ApiModelProperty(value = "优先级",required = true)
    private UseCasePriorityEnum priority;

    @NotNull(message = "[缺陷类型]不能为空")
    @ApiModelProperty(value = "缺陷类型",required = true)
    private BugTypeEnum type;

    @NotNull(message = "[缺陷来源]不能为空")
    @ApiModelProperty(value = "缺陷来源",required = true)
    private BugSourceEnum source;

    @NotNull(message = "[缺陷注入阶段]不能为空")
    @ApiModelProperty(value = "缺陷注入阶段",required = true)
    private BugInjectStageEnum injectStage;

    @NotNull(message = "[缺陷出现概率]不能为空")
    @ApiModelProperty(value = "缺陷出现概率",required = true)
    private BugProbabilityEnum probability;

    @ApiModelProperty(value = "附件文件路径，多个用逗号隔开")
    private String files;

    @NotBlank(message = "[缺陷详细说明]不能为空")
    @ApiModelProperty(value = "缺陷详细说明",required = true)
    private String detail;

    @ApiModelProperty("提出人姓名，前端无需传参")
    private String creator;

    @ApiModelProperty("提出人用户id，前端无需传参")
    private Integer creatorId;

    @ApiModelProperty(value = "提出时间，前端无需传参")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty(value = "当前处理人姓名",required = true)
    @NotBlank(message = "[当前处理人姓名]不能为空")
    private String currentProcessor;

    @ApiModelProperty(value = "当前处理人id",required = true)
    @NotNull(message = "[当前处理人id]不能为空")
    private Integer currentProcessorId;

    @ApiModelProperty(value = "缺陷归属人")
    private String owner;

    @ApiModelProperty(value = "缺陷解决人")
    private String solver;

    @ApiModelProperty(value = "缺陷解决人id")
    private Integer solverId;

    @ApiModelProperty(value = "缺陷解决时间，无需传参")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date solveTime;

    @ApiModelProperty(value = "缺陷解决结果")
    private BugSolveResultEnum solveResult;

    @ApiModelProperty(value = "解决时长，小时，后端计算，前端无需传参")
    private Double solveHours;

    @ApiModelProperty(value = "回归时长，小时，后端计算，前端无需传参")
    private Double regressionHours;

    @ApiModelProperty(value = "关联的测试计划id",required = true)
    private Integer planId;

    @ApiModelProperty(value = "关联的测试计划名称，无需传参，显示用")
    @TableField(exist = false)
    @JsonIgnore
    private String planName;

    @ApiModelProperty( value = "关联执行记录id",required = true)
    private Integer executeLogId;

    @NotNull(message = "[所属模块id]不能为空")
    @ApiModelProperty(value = "所属模块id",required = true)
    private Integer moduleId;

    @ApiModelProperty(value = "模块名称，无需传参，显示用")
    @TableField(exist = false)
    private String moduleName;

    @ApiModelProperty(value = "关联需求id",required = true)
    private Integer requirementId;

}
