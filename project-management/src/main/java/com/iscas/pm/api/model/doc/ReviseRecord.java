package com.iscas.pm.api.model.doc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonFormat;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.format.annotation.DateTimeFormat;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;


@Accessors(chain = true)
@ApiModel(value = "文档修订记录", description = "文档修订记录基本信息，对应project_demo库doc_revise_record表")
@TableName(value ="doc_revise_record")
@Data
public class ReviseRecord implements Serializable {


    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;


    @ApiModelProperty(value = "外键：模板id",required = true)
    private Integer templateId;


    @ApiModelProperty(value = "修订记录版本编号",required = true)
    @Size(max = 10,message = "版本编号长度不能大于10")
    private String version;


    @ApiModelProperty(value = "简要说明(变更内容和范围)")
    @Size(max = 25,message = "简要说明长度不能大于25")
    private String notes;

    @ApiModelProperty(value = "修订日期",required = true)
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd", timezone = "GMT+8")
    private Date date;

    @NotBlank(message = "修改人不能为空")
    @ApiModelProperty(value = "修改人",required = true)
    @Size(max = 11,message = "修改人长度不能大于11")
    private String mender;


    @ApiModelProperty(value = "批准人")
    @Size(max = 11,message = "批准人长度不能大于11")
    private String approver;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;


}