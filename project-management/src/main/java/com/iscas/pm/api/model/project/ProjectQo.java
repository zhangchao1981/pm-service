package com.iscas.pm.api.model.project;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Size;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description：查询条件封装
 */
@Data
public class ProjectQo {

    @ApiModelProperty(value = "项目名称")
    @Size(max = 20)
    private String projectName;


    @ApiModelProperty(value = "项目状态")
    @Size(max = 20)
    private ProjectStatusEnum status;

}
