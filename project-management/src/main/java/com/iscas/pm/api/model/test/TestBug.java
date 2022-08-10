package com.iscas.pm.api.model.test;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 测试缺陷表
* @TableName test_bug
*/
@Data
public class TestBug implements Serializable {

    @ApiModelProperty("缺陷编号")
    private Integer id;

    @NotBlank(message="[缺陷标题]不能为空")
    @Size(max= 50,message="编码长度不能超过50")
    @ApiModelProperty("缺陷标题")
    @Length(max= 50,message="编码长度不能超过50")
    private String title;

    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("当前处理人")
    @Length(max= 20,message="编码长度不能超过20")
    private String currentProcessor;

    @NotBlank(message="[缺陷状态]不能为空")
    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("缺陷状态")
    @Length(max= 20,message="编码长度不能超过20")
    private String status;

    @NotBlank(message="[提出人]不能为空")
    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("提出人")
    @Length(max= 20,message="编码长度不能超过20")
    private String creator;

    @NotBlank(message="[优先级]不能为空")
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("优先级")
    @Length(max= 32,message="编码长度不能超过32")
    private String priority;

    @NotBlank(message="[缺陷类型]不能为空")
    @Size(max= 32,message="编码长度不能超过32")
    @ApiModelProperty("缺陷类型")
    @Length(max= 32,message="编码长度不能超过32")
    private String type;

    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("附件文件路径，多个用逗号隔开")
    @Length(max= 255,message="编码长度不能超过255")
    private String files;

    @Size(max= 255,message="编码长度不能超过255")
    @ApiModelProperty("缺陷详细说明")
    @Length(max= 255,message="编码长度不能超过255")
    private String detail;

    @NotNull(message="[关联计划id]不能为空")
    @ApiModelProperty("关联计划id")
    private Integer planId;

    @NotNull(message="[关联用例id]不能为空")
    @ApiModelProperty("关联用例id")
    private Integer executeLogId;

    @NotBlank(message="[所属模块id]不能为空")
    @Size(max= 10,message="编码长度不能超过10")
    @ApiModelProperty("所属模块id")
    @Length(max= 10,message="编码长度不能超过10")
    private String moduleId;

    @NotNull(message="[关联需求id]不能为空")
    @ApiModelProperty("关联需求id")
    private Integer requirementId;

}
