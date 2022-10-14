package com.iscas.pm.api.model.test;

import javax.validation.constraints.NotBlank;
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
import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.iscas.pm.api.model.test.enums.UseCasePriorityEnum;
import com.iscas.pm.api.model.test.enums.UseCaseTypeEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;
import org.springframework.format.annotation.DateTimeFormat;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.NOT_NULL;

@Accessors(chain = true)
@ApiModel(value = "测试用例表", description = " 测试用例表，对应test_use_case表")
@TableName(value = "test_use_case", autoResultMap = true)
@Data
public class TestUseCase implements Serializable {
    @TableId(type = IdType.AUTO)
    @ApiModelProperty(value = "用例编号，后端自动生成")
    private Integer id;

    @Size(max = 100, message = "用例标题长度不能超过100")
    @ApiModelProperty(value = "用例标题", required = true)
    @NotBlank(message = "用例标题不能为空")
    private String title;

    @ApiModelProperty(value = "用例等级", required = true)
    @NotNull(message = "用例等级不能为空")
    private UseCasePriorityEnum level;

    @ApiModelProperty(value = "创建人,后端自动生成，前端无需传参")
    private String creator;

    @ApiModelProperty(value = "创建人id,后端自动生成，前端无需传参")
    private Integer creatorId;

    @NotNull(message = "[需求id]不能为空")
    @ApiModelProperty(value = "需求id", required = true)
    private Integer requirementId;

    @ApiModelProperty(value = "用例类型", required = true)
    @NotNull(message = "测试用例类型不能为空")
    private UseCaseTypeEnum type;

    @ApiModelProperty(value = "前置条件")
    private String precondition;

    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = FastjsonTypeHandler.class)
    @ApiModelProperty(value = "用例执行步骤", required = true)
    private List<ProgressStep> processStep;

    @ApiModelProperty(value = "所属模块id", required = true )
    @NotNull(message = "所属模块id不能为空")
    private Integer modularId;

    @ApiModelProperty("创建时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date createTime;

    @ApiModelProperty("更新时间")
    @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss")
    @JsonFormat(shape = JsonFormat.Shape.STRING, pattern = "yyyy-MM-dd HH:mm:ss", timezone = "GMT+8")
    private Date updateTime;

}
