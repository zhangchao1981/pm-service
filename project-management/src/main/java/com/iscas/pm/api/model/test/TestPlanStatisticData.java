package com.iscas.pm.api.model.test;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author by  lichang
 * @date 2022/8/12.
 */
@Accessors(chain = true)
@ApiModel(value = "测试计划统计信息", description = "缺陷统计，已测用例统计，通过率，执行进度")
@Data
public class TestPlanStatisticData {

    @ApiModelProperty("缺陷统计")
    private String bugStatistic;

    @ApiModelProperty("已测用例统计")
    private String testedCase;

    @ApiModelProperty("通过率")
    private Double passRate;

    @ApiModelProperty("执行进度")
    private Double executeProgress;

}
