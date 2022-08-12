package com.iscas.pm.api.model.test.param;

import com.iscas.pm.api.model.test.enums.BugInjectStageEnum;
import com.iscas.pm.api.model.test.enums.BugSolveResultEnum;
import com.iscas.pm.api.model.test.enums.BugTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author： zhangchao
 * @Date： 2022/8/12
 * @Description：
 */
@Data
@ApiModel("解决缺陷请求参数")
public class SolveBugParam {
    @ApiModelProperty(value = "缺陷id",required = true)
    @NotNull(message = "缺陷id不能为空")
    private Integer bugId;

    @ApiModelProperty(value = "缺陷解决结果")
    @NotNull(message = "缺陷解决结果不能为空")
    private BugSolveResultEnum solveResult;

    @NotNull(message = "[缺陷注入阶段]不能为空")
    @ApiModelProperty(value = "缺陷注入阶段",required = true)
    private BugInjectStageEnum injectStage;

    @NotNull(message = "[缺陷类型]不能为空")
    @ApiModelProperty(value = "缺陷类型",required = true)
    private BugTypeEnum type;

    @ApiModelProperty(value = "解决说明")
    private String explain;
}
