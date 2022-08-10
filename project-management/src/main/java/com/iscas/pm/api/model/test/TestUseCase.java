package com.iscas.pm.api.model.test;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.hibernate.validator.constraints.Length;

/**
* 测试用例表
* @TableName test_use_case
*/
@Data
public class TestUseCase implements Serializable {

    @NotNull(message="[用例编号]不能为空")
    @ApiModelProperty("用例编号")
    private Integer id;

    @Size(max= 100,message="编码长度不能超过100")
    @ApiModelProperty("用例标题")
    @Length(max= 100,message="编码长度不能超过100")
    private String title;

    @Size(max= 15,message="编码长度不能超过15")
    @ApiModelProperty("等级")
    @Length(max= 15,message="编码长度不能超过15")
    private String level;


    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("创建人")
    @Length(max= 20,message="编码长度不能超过20")
    private String creator;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @NotNull(message="[需求 id   外键]不能为空")
    @ApiModelProperty("需求 id   外键")
    private Integer requirementId;

    @Size(max= 20,message="编码长度不能超过20")
    @ApiModelProperty("用例类型")
    @Length(max= 20,message="编码长度不能超过20")
    private String type;

    @ApiModelProperty("处理步骤(记录)")
    private Object processStep;

    @ApiModelProperty("所属模块id")
    private Integer modularId;

}
