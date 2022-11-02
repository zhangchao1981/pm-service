package com.iscas.pm.api.model.doc.data;

import com.baomidou.mybatisplus.annotation.TableField;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.iscas.pm.api.model.dev.*;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;
import java.util.List;

/**
 * @author by  lichang
 * @Describe 用于文档生成时替换 DevRequirement
 * @date 2022/10/11.
 */


@Accessors(chain = true)
@ApiModel(value = "文档填充开发需求信息", description = "开发需求")
@Data
public class DocRequirement {

    @ApiModelProperty(value = "需求编号，系统自动生成，添加接口无需传参")
    private Integer id;

    public DocRequirement(DevRequirement devRequirement) {
        this.id = devRequirement.getId();
        this.name = devRequirement.getName();
        this.requirementDescription = devRequirement.getRequirementDescription();
        this.priority = devRequirement.getPriority();
        this.requirementType = devRequirement.getRequirementType();
        this.status = devRequirement.getStatus();
        this.devProgress = devRequirement.getDevProgress();
        this.scheduleHour = devRequirement.getScheduleHour();
        this.source = devRequirement.getSource();
        this.modularId = devRequirement.getModularId();
        this.devTasks = devRequirement.getDevTasks();
        this.role = devRequirement.getUseCaseExplain().getRole();
        this.precondition = devRequirement.getUseCaseExplain().getPrecondition();
        this.successScene = devRequirement.getUseCaseExplain().getSuccessScene();
        this.branchScene = devRequirement.getUseCaseExplain().getBranchScene();
        this.constraint = devRequirement.getUseCaseExplain().getConstraint();
    }

    @ApiModelProperty(value = "需求用例名称", required = true)
    private String name;

    @ApiModelProperty(value = "需求描述", required = true)
    private String requirementDescription;

    @ApiModelProperty(value = "责任人，前端无需传参")
    private String worker;

    @ApiModelProperty(value = "责任人的userId")
    private Integer userId;

    @ApiModelProperty(value = "优先级", required = true)
    private PriorityEnum priority;

    @ApiModelProperty(value = "需求类型", required = true)
    private RequirementTypeEnum requirementType;

    @ApiModelProperty(value = "是否是变更需求")
    private Boolean changed;

    @ApiModelProperty(value = "开发状态，前端无需传参，后端自动生成")
    private RequireStatusEnum status;

    @ApiModelProperty(value = "开发进度，前端无需传参，后端自动生成")
    private Double devProgress;

    @ApiModelProperty(value = "计划工时", required = true)
    private Double scheduleHour;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间", required = true)
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间", required = true)
    private Date endDate;

    @ApiModelProperty(value = "实际开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualStartDate;

    @ApiModelProperty(value = "实际结束日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualEndDate;

    @ApiModelProperty(value = "发生工时，前端无需传参，后端自动生")
    private Double happenedHour;

    @ApiModelProperty(value = "需求来源", required = true)
    private RequirementSourceEnum source;

    @ApiModelProperty(value = "模块id", required = true)
    private Integer modularId;

    @ApiModelProperty(value = "项目标识")
    private String projectId;

    private List<PoitlPicture> prototype;

    @JsonIgnore
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    @JsonIgnore
    @ApiModelProperty(value = "生成时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @TableField(exist = false)
    @ApiModelProperty(value = "返回值类型")
    private String type;

    @ApiModelProperty(value = "开发任务")
    @TableField(exist = false)
    private List<DevTask> devTasks;

    @ApiModelProperty(value = "需求角色", required = true)
    private String role;

    @ApiModelProperty(value = "前置条件", required = true)
    private List<UseCaseLine> precondition;

    @ApiModelProperty(value = "成功场景", required = true)
    private List<UseCaseLine> successScene;

    @ApiModelProperty(value = "分支场景", required = true)
    private List<UseCaseLine> branchScene;

    @ApiModelProperty(value = "设计约束", required = true)
    private List<UseCaseLine> constraint;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}