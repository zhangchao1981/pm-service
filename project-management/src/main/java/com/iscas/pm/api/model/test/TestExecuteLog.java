package com.iscas.pm.api.model.test;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;
import javax.validation.constraints.NotNull;

import java.io.Serializable;
import java.util.List;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import com.iscas.pm.api.model.test.enums.UseCasePriorityEnum;
import com.iscas.pm.api.model.test.enums.UseCaseTypeEnum;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;
import org.apache.ibatis.type.JdbcType;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.NOT_NULL;

/**
 * 测试用例执行记录表
 *
 * @TableName test_execute_log
 */
@Accessors(chain = true)
@ApiModel(value = "测试用例执行记录", description = "测试用例执行记录表，对应test_execute_log表")
@TableName(value = "test_execute_log", autoResultMap = true)
@Data
public class TestExecuteLog implements Serializable {

    @ApiModelProperty("id")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @NotNull(message = "导入的测试用例id不能为空")
    @ApiModelProperty(value = "导入的测试用例id", required = true)
    private Integer useCaseId;

    @ApiModelProperty(value = "是否通过" )
    private Boolean pass;

    @NotNull(message = "测试计划id不能为空")
    @ApiModelProperty(value = "测试计划id", required = true)
    private Integer planId;

    @Size(max = 20, message = "编码长度不能超过20")
    @ApiModelProperty(value = "测试人员姓名", required = true)
    private String testPerson;

    @NotNull(message = "测试人员的用户id不能为空")
    @ApiModelProperty(value = "测试人员的用户id", required = true)
    private Integer testPersonId;

    @Size(max = 100, message = "编码长度不能超过100")
    @NotBlank(message = "用例标题不能为空")
    @ApiModelProperty(value = "用例标题", required = true)
    private String title;

    @ApiModelProperty(value = "用例等级", required = true)
    @NotNull(message = "用例等级不能为空")
    private UseCasePriorityEnum level;

    @ApiModelProperty(value = "用例关联的需求id", required = true)
    @NotNull(message = "用例关联的需求id不能为空")
    private Integer requirementId;

    @ApiModelProperty(value = "用例类型", required = true)
    @NotNull(message = "用例类型不能为空")
    private UseCaseTypeEnum type;


    @ApiModelProperty(value = "用例执行步骤",required = true)
    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = FastjsonTypeHandler.class)
    private List<ProgressStep> processStep;


    @ApiModelProperty(value = "用例所属模块id",required = true)
    @NotNull(message = "用例所属模块id不能为空")
    private Integer modularId;

    @ApiModelProperty(value = "用例所属模块名称，无需传参，显示用")
    @TableField(exist = false)
    private String modularName;

    @ApiModelProperty(value = "缺陷数量",required = false)
    private Integer defaultCount;

    public TestExecuteLog() {
    }

    public TestExecuteLog(TestUseCase useCase ,Integer planId) {
        this.setType(useCase.getType());
        this.setProcessStep(useCase.getProcessStep());
        this.setLevel(useCase.getLevel());
        this.setRequirementId(useCase.getRequirementId());
        this.setTitle(useCase.getTitle());
        this.setPlanId(planId);
        this.setUseCaseId(useCase.getId());
        this.setModularId(useCase.getModularId());
        this.setTestPerson(RequestHolder.getUserInfo().getEmployeeName());
        this.setTestPersonId(RequestHolder.getUserInfo().getId());
    }

}
