package com.iscas.pm.api.model.test;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * 执行步骤
 *
 * @author by  lichang
 * @date 2022/8/10.
 */
@Data
public class ProgressStep {

    @ApiModelProperty("步骤序号")
    private Integer stepNumber;

    @ApiModelProperty("输入说明")
    private String inputExplain;

    @ApiModelProperty("期望结果")
    private String expectedOutcome;


}
