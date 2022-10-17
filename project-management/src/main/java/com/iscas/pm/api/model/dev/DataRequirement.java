package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableField;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.baomidou.mybatisplus.extension.handlers.FastjsonTypeHandler;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.apache.ibatis.type.JdbcType;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;
import java.util.List;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.IGNORED;
import static com.baomidou.mybatisplus.annotation.FieldStrategy.NOT_NULL;


/**
 * @TableName data_requirement
 */
@TableName(value = "data_requirement",autoResultMap = true)
@Data
public class DataRequirement implements Serializable {

    @ApiModelProperty(value = "id", notes = "不需前端传入")
    @TableId(type = IdType.AUTO)
    private Integer id;

    @TableField(jdbcType = JdbcType.VARCHAR, insertStrategy = NOT_NULL, typeHandler = FastjsonTypeHandler.class)
    @ApiModelProperty(value = "数据需求集合", required = true)
    @Valid
    private List<DataRequirementInfo> dataInfo;

    @ApiModelProperty(value = "数据需求名称",required = true)
    @NotBlank(message = "数据需求名称不能为空")
    private String requirementName;

    @ApiModelProperty(value = "开发需求id",required = true)
    @NotNull(message = "开发需求id不能为空")
    private Integer requireId;

    @TableField(exist = false)
    private static final long serialVersionUID = 1L;
}