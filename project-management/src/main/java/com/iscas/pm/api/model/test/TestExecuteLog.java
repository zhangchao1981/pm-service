package com.iscas.pm.api.model.test;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 测试用例执行记录表
* @TableName test_execute_log
*/
@Data
public class TestExecuteLog implements Serializable {

    @ApiModelProperty("id")
    private String id;

    @NotNull(message="[是否通过]不能为空")
    @ApiModelProperty("是否通过")
    private Integer pass;

    @ApiModelProperty("测试计划id")
    private Integer planId;

    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("测试人员姓名")
    @Length(max= 20,message="编码长度不能超过20")
    private String testPerson;

    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("用例标题")
    @Length(max= 100,message="编码长度不能超过100")
    private String title;

    @Size(max= 15,message="编码长度不能超过15")
    @ApiModelProperty("用例等级")
    @Length(max= 15,message="编码长度不能超过15")
    private String level;

    @ApiModelProperty("用例关联的需求id")
    private Integer requirementId;

    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("用例类型")
    @Length(max= 20,message="编码长度不能超过20")
    private String type;

    @ApiModelProperty("用例执行步骤")
    private Object processStep;

    @ApiModelProperty("用例所属模块id")
    private Integer modularId;

}
