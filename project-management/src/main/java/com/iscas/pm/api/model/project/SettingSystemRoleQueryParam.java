package com.iscas.pm.api.model.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author by  lichang
 * @date 2022/8/12.
 */
@Data
@ApiModel("分配角色参数")
public class SettingSystemRoleQueryParam {

    @ApiModelProperty(value = "目标用户id", required = true)
    @NotNull(message = "userId不能为空")
    private Integer userId;

    @ApiModelProperty(value = "要分配的角色列表", required = true)
    private List<Integer> roles;
}
