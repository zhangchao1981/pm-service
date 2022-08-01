package com.iscas.pm.api.model.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;


@Accessors(chain = true)
@ApiModel(value = "文档修订记录", description = "文档修订记录基本信息，对应project_demo库doc_revise_record表")
@TableName(value ="doc_revise_record")
@Data
public class ReviseRecord implements Serializable {


    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "外键：模板id")
    private Integer templateId;


    @ApiModelProperty(value = "修订记录版本编号")
    private String version;


    @ApiModelProperty(value = "简要说明(变更内容和范围)")
    private String notes;

    @ApiModelProperty(value = "修订日期")
    private Date date;

    @NotBlank(message = "修改人不能为空")
    @ApiModelProperty(value = "修改人")
    private String mender;


    @ApiModelProperty(value = "批准人")
    private String approver;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}