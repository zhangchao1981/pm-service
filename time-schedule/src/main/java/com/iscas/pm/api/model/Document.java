//package com.iscas.pm.api.model;
//
//import com.baomidou.mybatisplus.annotation.IdType;
//import com.baomidou.mybatisplus.annotation.TableField;
//import com.baomidou.mybatisplus.annotation.TableId;
//import com.baomidou.mybatisplus.annotation.TableName;
//import com.fasterxml.jackson.annotation.JsonFormat;
//import com.fasterxml.jackson.annotation.JsonIgnore;
//import com.iscas.pm.api.model.doc.DocumentTypeEnum;
//import io.swagger.annotations.ApiModel;
//import io.swagger.annotations.ApiModelProperty;
//import lombok.Data;
//import lombok.experimental.Accessors;
//import org.springframework.format.annotation.DateTimeFormat;
//
//import javax.validation.constraints.NotBlank;
//import javax.validation.constraints.NotNull;
//import javax.validation.constraints.Size;
//import java.io.Serializable;
//import java.util.Date;
//
//@Accessors(chain = true)
//@ApiModel(value = "项目文档", description = "项目文档基本信息，对应项目库doc_document表")
//@TableName(value ="doc_document")
//@Data
//public class Document implements Serializable {
//
//    @ApiModelProperty(value = "id")
//    @TableId(type = IdType.AUTO)
//    private Integer id;
//
//    @ApiModelProperty(value = "文档名称",required = true)
//    @NotBlank(message = "文档名称不能为空")
//    @Size(max = 25,message = "文档名称长度不能大于25")
//    private String name;
//
//    @ApiModelProperty(value = "上传者姓名，后端自动生成，前端无需传参")
//    @Size(max = 10,message = "上传者姓名长度不能大于10")
//    private String uploader;
//
//    @ApiModelProperty(value = "版本号")
//    @Size(max = 10,message = "版本号长度不能大于10")
//    private String version;
//
//    @ApiModelProperty(value = "文档类型",required = true)
//    @NotNull(message = "文档类型不能为空")
//    private DocumentTypeEnum type;
//
//    @ApiModelProperty(value = "所属的目录id",required = true)
//    @NotNull(message = "文档目录id不能为空")
//    private Integer directoryId;
//
//    @ApiModelProperty(value = "文档路径",required = true)
//    @Size(max = 255,message = "文档路径方长度不能大于255")
//    private String path;
//
//    @ApiModelProperty(value = "生成时间，后端自动生成，前端无需传参")
//    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private Date createTime;
//
//    @JsonIgnore
//    @ApiModelProperty(value = "最后更新时间,后端自动生成，前端无需传参")
//    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
//    private Date updateTime;
//
//    @TableField(exist = false)
//    private static final long serialVersionUID = 1L;
//}