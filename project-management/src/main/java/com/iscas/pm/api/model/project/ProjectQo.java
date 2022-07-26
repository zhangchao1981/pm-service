package com.iscas.pm.api.model.project;

import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.NonNull;

import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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
    private ProjectStatusEnum status;

    @NotNull
    @ApiModelProperty(value = "分页信息")
    private Page page;

}
