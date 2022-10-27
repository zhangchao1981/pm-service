package com.iscas.pm.api.model.test.param;

import com.iscas.pm.api.model.test.enums.UseCaseTypeEnum;
import io.swagger.annotations.ApiModelProperty;
import io.swagger.models.auth.In;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @Author：lichang
 * @Date： 2022/10/24
 * @Description： 导入测试计划时查询待选的测试用例
 */
@Data
public class UseCaseForPlanQueryParam {

    @ApiModelProperty(value = "要查询的用例标题或用例编号")
    private String titleOrId;

    @ApiModelProperty(value = "要查询的用例类型")
    private String useCaseType;

    @ApiModelProperty(value = "创建人id")
    private Integer creatorId;

    @ApiModelProperty(value = "所属模块id")
    private List<Integer> modularList;

    @ApiModelProperty(value = "所属开发需求")
    private Integer requirementId;



}
