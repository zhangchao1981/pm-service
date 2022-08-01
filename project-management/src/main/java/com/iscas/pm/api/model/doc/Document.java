package com.iscas.pm.api.model.doc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@Accessors(chain = true)
@ApiModel(value = "项目文档", description = "项目文档基本信息，对应项目库doc_document表")
@TableName(value ="doc_document")
@Data
public class Document implements Serializable {

    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "文档名称")
    @NotBlank(message = "文档名称不能为空")
    private String name;

    @ApiModelProperty(value = "上传者姓名，后端自动生成，前端无需传参")
    private String uploader;

    @ApiModelProperty(value = "版本号")
    private String version;

    @ApiModelProperty(value = "文档类型")
    @NotNull(message = "文档类型不能为空")
    private DocumentTypeEnum type;

    @JsonIgnore
    @ApiModelProperty(value = "所属的目录id")
    @NotNull(message = "文档目录id不能为空")
    private Integer directoryId;

    @ApiModelProperty(value = "文档路径，链接类型的文档需要传参，其他类型不需要")
    private String path;

    @ApiModelProperty(value = "生成时间")
    private Date createTime;

    @JsonIgnore
    @ApiModelProperty(value = "最后更新时间")
    private Date updateTime;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}