package com.iscas.pm.api.model.test.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotNull;
import java.util.Date;

/**
 * @author by  lichang
 * @date 2022/8/10.
 */
@Data
public class TestBugQueryParam {

    @ApiModelProperty(value = "当前页", required = true)
    @NotNull(message = "显示页面的页码不能为空")
    @Min(value = 1, message = "当前页码不能小于1")
    private Integer pageNum;

    @ApiModelProperty(value = "每页显示记录条数", required = true)
    @NotNull(message = "每页显示的条数不能为空")
    @Min(value = 1, message = "每页显示的条数不能小于1")
    private Integer pageSize;

    @ApiModelProperty(value = "缺陷编号")
    private Integer id;

    @ApiModelProperty(value = "缺陷标题")
    private String title;

    @ApiModelProperty(value = "当前处理人")
    private String currentProcessor;

    @ApiModelProperty(value = "缺陷状态")
    private String status;

    @ApiModelProperty("提出人")
    private String creator;

    @ApiModelProperty(value = "优先级")
    private String priority;

    @ApiModelProperty(value = "缺陷类型")
    private String type;

    @ApiModelProperty(value = "关联计划id")
    private Integer planId;

    @ApiModelProperty(value = "所属模块id")
    private Integer moduleId;

    @ApiModelProperty(value = "关联需求id")
    private Integer requirementId;

}
