package com.iscas.pm.api.model.test;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* 测试计划表
* @TableName test_plan
*/
@Data
public class TestPlan implements Serializable {

    @ApiModelProperty("id")
    private Integer id;

    @NotBlank(message="[测试计划名称]不能为空")
    @ApiModelProperty("测试计划名称")
    private String name;

    @NotNull(message="[测试类型]不能为空")
    @ApiModelProperty("测试类型")
    private String testType;

    @ApiModelProperty("负责人")
    private String worker;

    @ApiModelProperty("缺陷统计")
    private String bugStatistic;

    @ApiModelProperty("已测用例统计")
    private String testedCase;

    @ApiModelProperty("通过率")
    private Double passRate;

    @ApiModelProperty("执行进度")
    private Double executeProgress;

    @NotNull(message="[开始时间]不能为空")
    @ApiModelProperty("开始时间")
    private Date startTime;

    @NotNull(message="[结束时间]不能为空")
    @ApiModelProperty("结束时间")
    private Date endTime;

    @NotNull(message="[创建时间]不能为空")
    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("最后更新时间")
    private Date updateTime;

}
