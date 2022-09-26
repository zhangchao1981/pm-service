package com.iscas.pm.api.model.project;

import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableName;
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iscas.pm.common.core.util.validation.CheckTimeInterval;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.*;
import java.io.Serializable;
import java.math.BigDecimal;
import java.util.Date;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 项目基本信息PO
 */
@Data
@Accessors(chain = true)
@ApiModel(value = "项目信息", description = "项目基本信息，对应主库project表")
@CheckTimeInterval(beginTime = {"startDate"}, endTime = {"endDate"}, message = "项目开始日期不能大于项目结束日期")
@TableName("pm_project")
public class Project implements Serializable {

    @ApiModelProperty(value = "项目编号,不允许修改", notes = "对应数据库名，小写英文.", required = true)
    @NotBlank(message = "项目编号不能为空")
    @Size(max = 10,message = "项目编号长度不能大于10")
    private String id;

    @ApiModelProperty(value = "项目名称", required = true)
    @NotBlank(message = "项目名称不能为空")
    @Size(max = 25,message = "项目名称长度不能大于25")
    private String name;

    @ApiModelProperty(value = "项目密级", required = true)
    @NotNull(message = "项目密级不能为空")
    private SecretLevelEnum secretLevel;

    @ApiModelProperty(value = "项目类型", required = true)
    @NotNull(message = "项目类型不能为空")
    private ProjectTypeEnum projectType;

    @ApiModelProperty(value = "项目状态")
    private ProjectStatusEnum status;

    @ApiModelProperty(value = "项目开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @ApiModelProperty(value = "项目结束日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;

    @ApiModelProperty(value = "项目负责人", required = true)
    @NotBlank(message = "项目负责人不能为空")
    @Size(max = 10,message = "项目负责人长度不能大于10")
    private String managerName;


    @ApiModelProperty(value = "项目概述")
    @Size(max = 5000,message = "项目概述长度不能大于5000")
    private String description;

    @ApiModelProperty(value = "研制单位")
    @Size(max = 255,message = "研制单位长度不能大于255")
    private String manufacture;

    @ApiModelProperty(value = "项目提出方")
    @Size(max = 255,message = "项目提出方长度不能大于255")
    private String projectProvider;

    @ApiModelProperty(value = "需求提出方")
    @Size(max = 255,message = "需求提出方长度不能大于255")
    private String requirementProvider;

    @ApiModelProperty(value = "合同编号")
    @Size(max = 255,message = "合同编号长度不能大于255")
    private String contractId;

    @ApiModelProperty(value = "合同金额")
    @DecimalMin(value = "0.0", message = "合同金额大小不能为负")
    private BigDecimal contractAmount;

    @ApiModelProperty(value = "创建人的用户名")
    @Size(max = 255,message = "创建人的用户名不能大于255")
    private String createUser;

    @JsonIgnore
    @ApiModelProperty(value = "创建时间")
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty(value = "最后修改时间")
    private Date updateTime;

    @ApiModelProperty(value = "审核意见")
    @Size(max = 5000,message = "审核意见不能大于5000")
    private String approveComments;
}