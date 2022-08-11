package com.iscas.pm.api.model.test;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author： zhangchao
 * @Date： 2022/8/11
 * @Description： 用例执行步骤
 */
@Data
public class ProgressStep {
    @ApiModelProperty("步骤序号")
    private Integer stepNumber;

    @ApiModelProperty("输入说明")
    private String inputExplain;

    @ApiModelProperty("预期结果")
    private String expectedResult;
}