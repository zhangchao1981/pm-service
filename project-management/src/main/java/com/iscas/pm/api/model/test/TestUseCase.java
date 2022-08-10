package com.iscas.pm.api.model.test;

import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;

import java.util.Date;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;
import org.hibernate.validator.constraints.Length;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.NOT_NULL;


@Accessors(chain = true)
@ApiModel(value = "测试用例表", description = " 测试用例表，对应test_use_case表")
@TableName(value = "test_use_case")
@Data
public class TestUseCase implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "用例编号")
    private Integer id;

    @Size(max = 100, message = "编码长度不能超过100")
    @ApiModelProperty(value = "用例标题", required = true)
    private String title;

    @ApiModelProperty(value = "等级", required = true)
    private UseCaseLevelEnum level;

    @ApiModelProperty(value = "创建人")
    private String creator;

    @ApiModelProperty("创建时间")
    private Date createTime;

    @ApiModelProperty("更新时间")
    private Date updateTime;

    @NotNull(message = "[需求id]不能为空")
    @ApiModelProperty(value = "需求id", required = true)
    private Integer requirementId;

    @ApiModelProperty(value = "用例类型", required = true)
    private UseCaseTypeEnum type;


    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = FastjsonTypeHandler.class)
    @ApiModelProperty(value = "处理步骤(记录)", required = true)
    private List<ProgressStep> processStep;

    @ApiModelProperty(value = "所属模块id" )
    private Integer modularId;

}
