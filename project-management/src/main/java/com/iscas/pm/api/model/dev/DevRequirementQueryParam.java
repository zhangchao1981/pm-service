package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Size;

import static com.baomidou.mybatisplus.annotation.FieldStrategy.IGNORED;

/**
 * @author by  lichang
 * @date 2022/10/9.
 */
@Data
public class DevRequirementQueryParam {
    @ApiModelProperty(value = "查询类型",required = true)
    private DevRequirementQueryTypeEnum queryType;

    @ApiModelProperty(value = "要查询内容的名称或编号",required = true)
    private String nameOrCode;

    @ApiModelProperty(value = "责任人,传参是人员姓名",required = true)
    private String worker;

    @ApiModelProperty(value = "开发需求状态 ",required = true)
    private RequireStatusEnum  statusEnum;

    @ApiModelProperty(value = "开发需求状态 ",required = true)
    private RequireStatusEnum  statusEnum;


}
