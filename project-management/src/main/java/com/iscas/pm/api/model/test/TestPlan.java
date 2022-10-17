package com.iscas.pm.api.model.test;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iscas.pm.api.model.test.enums.TestTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;


@Accessors(chain = true)
@ApiModel(value = "测试计划", description = " 测试计划，对应test_plan表")
@TableName(value = "test_plan")
@Data
public class TestPlan implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;

    @NotBlank(message = "[测试计划名称]不能为空")
    @ApiModelProperty(value = "测试计划名称", required = true)
    private String name;

    @ApiModelProperty(value = "测试计划类型", required = true)
    private TestTypeEnum testType;

    @ApiModelProperty(value = "负责人", required = true)
    private String worker;

    @ApiModelProperty(value = "负责人id", required = true)
    private String workerId;

    @ApiModelProperty("缺陷统计，后端计算，无需传参")
    private String bugStatistic;

    @ApiModelProperty("已测用例统计，后端计算，无需传参")
    private String testedCase;

    @ApiModelProperty("通过率，后端计算，无需传参")
    private Double passRate;

    @ApiModelProperty("执行进度，后端计算，无需传参")
    private Double executeProgress;

    @NotNull(message = "[开始时间]不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间", required = true)
    private Date startTime;

    @NotNull(message = "[结束时间]不能为空")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间", required = true)
    private Date endTime;

    @JsonIgnore
    @ApiModelProperty("创建时间")
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty("最后更新时间")
    private Date updateTime;

    public void inputStatisticData(TestPlanStatisticData statisticData) {
        this.setPassRate(statisticData.getPassRate());
        this.setTestedCase(statisticData.getTestedCase());
        this.setBugStatistic(statisticData.getBugStatistic());
        this.setExecuteProgress(statisticData.getExecuteProgress());
    }

}
