package com.iscas.pm.api.model.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author： zhangchao
 * @Date： 2022/9/23
 * @Description：
 */
@Data
public class ProjectMember {

    @ApiModelProperty(value = "人员名称")
    private String employeeName;

    @ApiModelProperty(value = "角色",required = true)
    private String  role;

    @ApiModelProperty(value = "角色说明",required = true)
    private String  description;

}
