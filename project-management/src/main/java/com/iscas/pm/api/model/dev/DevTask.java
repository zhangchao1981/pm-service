package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.iscas.pm.api.model.projectPlan.TaskStatusEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.io.Serializable;
import java.util.Date;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.IGNORED;

@ApiModel(value = "开发任务", description = "开发任务表，对应project_demo库dev_requirement表")
@TableName(value = "dev_task")
@Data
public class DevTask implements Serializable {

    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "任务名称不能为空")
    @ApiModelProperty(value = "任务名称",required = true)
    @Size(max = 32,message = "任务名称长度不能大于32")
    private String name;

    @NotBlank(message = "责任人不能为空")
    @ApiModelProperty(value = "责任人,传参是人员姓名",required = true)
    @Size(max = 11,message = "责任人长度不能大于11")
    @TableField(updateStrategy = IGNORED)
    private String worker;

    @NotNull(message = "责任人对应userId不能为空")
    @ApiModelProperty(value = "责任人userId",required = true)
    @TableField(updateStrategy = IGNORED)
    private Integer workerId;

    @ApiModelProperty(value = "任务状态，前端无需传参，后端自动生成")
    @TableField(updateStrategy = IGNORED)
    private TaskStatusEnum status;

    @ApiModelProperty(value = "开发进度")
    @TableField(updateStrategy = IGNORED)
    private Integer devProgress;

    @ApiModelProperty(value = "计划工时",required = true)
    @TableField(updateStrategy = IGNORED)
    private Double scheduleHour;

    @ApiModelProperty(value = "发生工时，前端无需传参，后端自动生成")
    private Double happenedHour;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "开始时间",required = true)
    @TableField(updateStrategy = IGNORED)
    private Date startDate;

    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @ApiModelProperty(value = "结束时间",required = true)
    @TableField(updateStrategy = IGNORED)
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

    @JsonIgnore
    @ApiModelProperty(value = "开发需求id",required = true)
    @NotNull(message = "开发需求id不能为空")
    private Integer requireId;

    @TableField(exist = false)
    @ApiModelProperty(value = "返回值类型")
    private String type;

    public DevTask() {
        type= String.valueOf(DevTypeEnum.Task);
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}