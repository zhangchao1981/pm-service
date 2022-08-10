package com.iscas.pm.api.model.dev;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * @author by  lichang
 * @date 2022/8/4.
 */
@Data
public class UseCase {
    @ApiModelProperty(value = "需求角色",required = true)
    private String  role;

    @ApiModelProperty(value = "前置条件",required = true)
    private List<UseCaseLine>  precondition;

    @ApiModelProperty(value = "成功场景",required = true)
    private List<UseCaseLine> successScene;

    @ApiModelProperty(value = "分支场景",required = true)
    private List<UseCaseLine> branchScene;

    @ApiModelProperty(value = "业务规则设计约束",required = true)
    private List<UseCaseLine> constraint;

}
