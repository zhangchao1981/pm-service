package com.iscas.pm.auth.domain.user;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.beans.factory.annotation.Value;

import javax.validation.constraints.NotBlank;

/**
 * @author by  lichang
 * @date 2022/7/27.
 */
@Data
@Accessors(chain = true)
public class ListQueryCondition {
    @ApiModelProperty(value = "用户名")
    private String userName;

    @ApiModelProperty(value = "人员姓名")
    private String employeeName;

    @ApiModelProperty(value = "当前页")
    private  Integer pageNum;

    @ApiModelProperty(value = "页面大小")
    private  Integer pageSize;

    @ApiModelProperty(value = "账号状态")
    private   String   status;

    @ApiModelProperty(value = "人员所在部门id")
    private String departmentId;

}
