package com.iscas.pm.auth.model;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * @author by  lichang
 * @date 2022/8/12.
 */
@Data
@ApiModel("分配权限参数")
public class SettingPermissionsQueryParam {

    @NotNull(message = "目标角色id不能为空")
    @ApiModelProperty(value = "目标角色id", required = true)
    private Integer roleId;

    @NotEmpty(message = "要分配的权限列表不能为空")
    @ApiModelProperty(value = "要分配的权限列表", required = true)
    private List<String> permissionIds;

}
