package com.iscas.pm.api.model.doc;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.util.Date;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iscas.pm.api.model.doc.param.AddTemplateParam;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

@Accessors(chain = true)
@ApiModel(value = "文档生成模板", description = "文档生成模板，对应project_demo库doc_template表")
@TableName(value ="doc_template")
@Data
public class DocTemplate implements Serializable {

    @ApiModelProperty(value = "id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotBlank(message = "维护人不能为空")
    @ApiModelProperty(value = "维护人",required = true)
    @Size(max = 25,message = "接收方长度不能大于25")
    private String maintainer;

    @NotBlank(message = "模板名称不能为空")
    @ApiModelProperty(value = "模板名称",required = true)
    @Size(max = 25,message = "接收方长度不能大于25")
    private String name;

    @ApiModelProperty(value = "模板描述",required = false)
    @Size(max = 25,message = "接收方长度不能大于25")
    private String description;


    @ApiModelProperty(value = "模板文件",required = true)
    @Size(max = 25,message = "接收方长度不能大于25")
    private String path;


    @JsonIgnore
    @ApiModelProperty("模板创建时间")
    private Date createTime;


    @JsonIgnore
    @ApiModelProperty("模板最后更新时间")
    private Date updateTime;

    public  void addTemplateParam(AddTemplateParam param){
        this.maintainer=param.getMaintainer();
        this.description=param.getDescription();
        this.name=param.getName();
        this.path=param.getPath();
    }

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}