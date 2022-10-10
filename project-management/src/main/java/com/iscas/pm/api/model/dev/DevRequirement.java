package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.iscas.pm.common.core.util.validation.CheckTimeInterval;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.IGNORED;
import static com.baomidou.mybatisplus.annotation.FieldStrategy.NOT_NULL;

@TableName(value = "dev_requirement", autoResultMap = true)
@Accessors(chain = true)
@ApiModel(value = "开发需求表", description = "开发需求表板，对应项目库dev_requirement表")
@CheckTimeInterval(beginTime = {"startDate"}, endTime = {"endDate"}, message = "项目开始日期不能大于项目结束日期")
@Data
public class DevRequirement implements Serializable {

    @ApiModelProperty(value = "需求编号，系统自动生成，添加接口无需传参")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "需求用例名称",required = true)
    @Size(max = 25,message = "需求用例名称长度不能大于25")
    @NotBlank
    private String name;

    @ApiModelProperty(value = "需求描述",required = true)
    @Size(max = 150,message = "需求描述长度不能大于150")
    private String requirementDescription;

    @ApiModelProperty(value = "责任人，前端无需传参")
    private String worker;

    @ApiModelProperty(value = "责任人的userId")
    private Integer userId;

    @ApiModelProperty(value = "优先级",required = true)
    @NotNull
    private PriorityEnum priority;

    @ApiModelProperty(value = "需求类型",required = true)
    @NotNull
    private RequirementTypeEnum requirementType;

    @TableField("is_change")
    @ApiModelProperty(value = "是否是变更需求")
    private Boolean changed;

    @ApiModelProperty(value = "开发状态，前端无需传参，后端自动生成")
    private RequireStatusEnum status;

    @ApiModelProperty(value = "开发进度，前端无需传参，后端自动生成")
    private Double devProgress;

    @ApiModelProperty(value = "计划工时",required = true)
    private Double scheduleHour;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间",required = true)
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间",required = true)
    private Date endDate;

    @ApiModelProperty(value = "实际开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone ="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date actualStartDate;

    @ApiModelProperty(value = "实际结束日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone ="GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @TableField(updateStrategy = IGNORED)
    private Date actualEndDate;

    @ApiModelProperty(value = "发生工时，前端无需传参，后端自动生")
    private Double happenedHour;

    @ApiModelProperty(value = "需求来源",required = true)
    private RequirementSourceEnum source;

    @ApiModelProperty(value = "用例说明",required = true)
    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = FastjsonTypeHandler.class)
    private UseCaseExplain useCaseExplain;

    @ApiModelProperty(value = "模块id",required = true)
    @NotNull
    private Integer modularId;

    @ApiModelProperty(value = "原型设计图路径，多个用逗号隔开")
    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = FastjsonTypeHandler.class)
    private List<String> prototype;

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


    public DevRequirement() {
        type= String.valueOf(DevTypeEnum.Requirement);
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}