package com.iscas.pm.auth.model;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;

/**
 * @author by  lichang
 * @date 2022/7/27.
 */
@Data
@Accessors(chain = true)
public class UserQueryParam {
    @ApiModelProperty(value = "当前页")
    @NotNull(message = "显示页面的页码不能为空")
    @Min(value = 1, message = "当前页码不能小于1")
    private Integer pageNum;

    @ApiModelProperty(value = "每页显示记录条数")
    @NotNull(message = "每页显示的条数不能为空")
    @Min(value = 1, message = "每页显示的条数不能小于1")
    private Integer pageSize;

    @ApiModelProperty(value = "用户名或姓名")
    private String name;

    @ApiModelProperty(value = "账号状态")
    private String status;

    @ApiModelProperty(value = "人员所在部门id")
    private String departmentId;

}
