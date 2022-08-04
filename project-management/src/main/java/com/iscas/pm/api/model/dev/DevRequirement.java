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

import java.io.Serializable;
import java.util.Date;
import java.util.List;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.NOT_NULL;


@TableName(value = "dev_requirement",autoResultMap = true)
@Accessors(chain = true)
@ApiModel(value = "开发需求表", description = "开发需求表板，对应project_demo库dev_requirement表")
@CheckTimeInterval(beginTime = {"startDate"},endTime = {"endDate"},message = "项目开始日期不能大于项目结束日期")
@Data
public class DevRequirement implements Serializable {

    @ApiModelProperty(value = "需求编号  6位数")
    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "需求用例名称")
    private String name;


    @ApiModelProperty(value = "责任人")
    private String worker;


    @ApiModelProperty(value = "优先级")
    private PriorityEnum priority;


    @ApiModelProperty(value = "需求类型")
    private String requirementType;

    @TableField("is_change")
    @ApiModelProperty(value = "变更需求  0: 无变更 1:有变更")
    private Boolean changed;

    @ApiModelProperty(value = "状态")
    private RequireStatusEnum status;


    @ApiModelProperty(value = "开发进度")
    private Double devProgress;


    @ApiModelProperty(value = "计划工时")
    private Double scheduleHour;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间")
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间")
    private Date endDate;


    @ApiModelProperty(value = "发生工时")
    private Double happendHour;

    @JsonIgnore
    @ApiModelProperty(value = "需求来源")
    private String source;

    @JsonIgnore
    @ApiModelProperty(value = "用例说明")
    private String useCase;

    @JsonIgnore
    @ApiModelProperty(value = "模块id")
    private Integer modularId;

    @JsonIgnore
    @ApiModelProperty(value = "原型设计图路径，多个用逗号隔开")
    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = FastjsonTypeHandler.class)
    private List<String>  prototype;


    @JsonIgnore
    @ApiModelProperty(value = "更新时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date updateTime;

    @JsonIgnore
    @ApiModelProperty(value = "生成时间")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd",timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createTime;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value = "需求角色")
    private String  role;


    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value = "前置条件")
    private List<String>  precondition;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value = "成功场景")
    private List<String> successScene;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value = "分支场景")
    private List<String> branchScene;

    @JsonIgnore
    @TableField(exist = false)
    @ApiModelProperty(value = "业务规则设计约束")
    private List<String> constraint;


    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}