package com.iscas.pm.api.model.projectPlan;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Max;
import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;


@TableName(value ="task_feedback")
@ApiModel("任务反馈")
@Data
public class TaskFeedback implements Serializable {
    @ApiModelProperty("任务反馈id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "反馈人姓名，前端无需填写，后端自动生成")
    private String personName;

    @ApiModelProperty("用户id，前端无需填写，后端自动生成")
    private Integer userId;

    @ApiModelProperty("反馈日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date date;

    @ApiModelProperty("当日进度（0-100）")
    @NotNull
    @Min(0)
    @Max(100)
    private Integer progress;

    @ApiModelProperty("当日花费工时")
    @NotNull
    @Min(0)
    @Max(8)
    private float workingHour;

    @ApiModelProperty("工作内容说明")
    private String workingContent;

    @ApiModelProperty("关联的计划任务id")
    private Integer planTaskId;

    @JsonIgnore
    @ApiModelProperty("填写反馈时间")
    private Date createTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;

}