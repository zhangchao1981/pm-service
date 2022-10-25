package com.iscas.pm.api.model.test.param;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
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

    @ApiModelProperty(value = "缺陷编号，前端无需传参",required = false)
    @JsonIgnore
    private Integer id;

    @ApiModelProperty(value = "缺陷标题，前端无需传参",required = false)
    @JsonIgnore
    private String title;

    @ApiModelProperty(value = "缺陷标题或编号")
    private String titleOrId;

    @ApiModelProperty(value = "当前处理人")
    private String currentProcessor;

    @ApiModelProperty(value = "缺陷状态")
    private String status;

    @ApiModelProperty("提出人")
    private String creator;

    @ApiModelProperty(value = "查询提出时间最小值")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date minCreateTime;

    @ApiModelProperty(value = "查询提出时间最大值")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date maxCreateTime;

    @ApiModelProperty(value = "优先级")
    private String priority;

    @ApiModelProperty(value = "缺陷类型")
    private String type;

    @ApiModelProperty(value = "关联计划id")
    private Integer planId;

    @ApiModelProperty(value = "所属模块id")
    private Integer moduleId;

    @ApiModelProperty(value = "缺陷注入阶段")
    private String injectStage;

    @ApiModelProperty(value = "关联需求id")
    private Integer requirementId;

}
