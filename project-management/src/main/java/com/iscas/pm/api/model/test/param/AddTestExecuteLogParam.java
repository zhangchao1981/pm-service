package com.iscas.pm.api.model.test.param;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author by  lichang
 * @date 2022/8/11.
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "添加测试计划执行记录所需参数")
public class AddTestExecuteLogParam {

        @ApiModelProperty(value = "导入测试用例idList", required = true)
        @NotEmpty(message = "要导入的测试用例id为空")
        List<Integer> idList;

        @ApiModelProperty(value = "当前测试计划id" ,required = true)
        @NotNull(message = "当前测试计划id为空")
        Integer planId;
    }
