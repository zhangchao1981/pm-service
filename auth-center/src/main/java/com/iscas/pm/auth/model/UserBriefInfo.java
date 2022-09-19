package com.iscas.pm.auth.model;

import com.iscas.pm.common.core.model.User;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @author by  lichang
 * @date 2022/9/9.
 */
@Data
@ApiModel("添加用户时候选名单上显示的信息")
@Accessors(chain = true)
public class UserBriefInfo {
    @ApiModelProperty(value = "人员姓名，不允许修改")
    private String employName;

    @ApiModelProperty(value = "人员所在部门名")
    private String departmentName;

    @ApiModelProperty(value = "用户id")
    private Integer userId;

    @ApiModelProperty(value = "用户名(重名区分)")
    private String userName;
}
