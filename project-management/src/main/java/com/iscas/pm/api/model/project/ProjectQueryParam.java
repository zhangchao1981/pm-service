package com.iscas.pm.api.model.project;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.bouncycastle.cms.PasswordRecipientId;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description：查询条件封装
 */
@Data
public class ProjectQueryParam {

    @ApiModelProperty(value = "项目名称")
    @Size(max = 20)
    private String projectName;

    @ApiModelProperty(value = "项目状态")
    private String status;

    @ApiModelProperty(value = "当前页", required = true)
    @NotNull(message = "显示页面的页码不能为空")
    @Min(value = 1, message = "当前页码不能小于1")
    private Integer pageNum;

    @ApiModelProperty(value = "每页显示记录条数", required = true)
    @NotNull(message = "每页显示的条数不能为空")
    @Min(value = 1, message = "每页显示的条数不能小于1")
    private Integer pageSize;

    @ApiModelProperty(value = "用户id")
    @JsonIgnore
    private Integer userId;

}
