package com.iscas.pm.api.model.test;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.hibernate.validator.constraints.Length;

/**
 * 测试用例执行记录表
 *
 * @TableName test_execute_log
 */
@Accessors(chain = true)
@ApiModel(value = "测试用例执行记录", description = "测试用例执行记录表，对应test_execute_log表")
@TableName(value = "test_execute_log")
@Data
public class TestExecuteLog implements Serializable {

    @ApiModelProperty("id")
    @TableId(type = IdType.AUTO)
    private String id;

    @NotNull(message = "[是否通过]不能为空")
    @ApiModelProperty(value = "是否通过", required = true)
    private Integer pass;

    @ApiModelProperty(value = "测试计划id", required = true)
    private Integer planId;

    @Size(max = 20, message = "编码长度不能超过20")
    @ApiModelProperty(value = "测试人员姓名", required = true)
    private String testPerson;

    @Size(max = 100, message = "编码长度不能超过100")
    @ApiModelProperty(value = "用例标题", required = true)
    private String title;

    @Size(max = 15, message = "编码长度不能超过15")
    @ApiModelProperty("用例等级")
    private String level;

    @ApiModelProperty("用例关联的需求id")
    private Integer requirementId;

    @Size(max = 20, message = "编码长度不能超过20")
    @ApiModelProperty("用例类型")
    @Length(max = 20, message = "编码长度不能超过20")
    private String type;

    @ApiModelProperty("用例执行步骤")
    private Object processStep;

    @ApiModelProperty("用例所属模块id")
    private Integer modularId;

}
