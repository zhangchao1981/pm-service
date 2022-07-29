package com.iscas.pm.api.model.project;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDate;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

@Accessors(chain = true)
@ApiModel(value = "项目文档", description = "项目文档基本信息，对应demo库document表")
@TableName(value ="document")
@Data
public class Document implements Serializable {

    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;



    @ApiModelProperty(value = "文档名称")
    private String name;



    @ApiModelProperty(value = "上传者姓名")
    private String uploader;



    @ApiModelProperty(value = "生成时间")
    private LocalDate createTime;



    @ApiModelProperty(value = "版本号")
    private String version;



    @ApiModelProperty(value = "文档类型")
    private String type;


    @ApiModelProperty(value = "所属的目录id")
    private Integer directoryId;


    @ApiModelProperty(value = "文档路径")
    private String path;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}