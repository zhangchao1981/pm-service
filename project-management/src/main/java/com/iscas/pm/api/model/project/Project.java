package com.iscas.pm.api.model.project;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.iscas.pm.common.core.util.validation.CheckTimeInterval;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.Date;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 项目基本信息PO
 */
@Data
@ApiModel(value = "项目信息", description = "项目基本信息，对应主库project表")
@CheckTimeInterval(beginTime = {"startDate"},endTime = {"endDate"},message = "项目开始日期不能大于项目结束日期")
public class Project implements Serializable {

    @ApiModelProperty(value = "项目编号")
    @NotBlank(message = "项目编号不能为空")
    private String id;

    @ApiModelProperty(value = "项目名称")
    @NotBlank(message = "项目名称不能为空")
    private String name;

    @ApiModelProperty(value = "项目密级")
    @NotNull(message = "项目密级不能为空")
    private SecretLevel secretLevel;

    @ApiModelProperty(value = "项目类型")
    @NotNull(message = "项目类型不能为空")
    private ProjectType projectType;

    @ApiModelProperty(value = "项目开始日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date startDate;

    @ApiModelProperty(value = "项目结束日期")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd")
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date endDate;



}