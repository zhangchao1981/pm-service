package com.iscas.pm.api.model.project;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.springframework.validation.annotation.Validated;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.io.Serializable;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 项目基本信息PO
 */
@Data
@ApiModel(value = "项目信息", description = "项目基本信息，对应主库project表")
public class Project implements Serializable {

    @ApiModelProperty(value = "项目编号",notes = "自动生成，前端无需填写")
    private String id;

    @ApiModelProperty(value = "项目名称")
    @NotBlank(message = "项目名称不能为空")
    private String name;

    @ApiModelProperty(value = "项目密级")
    @NotNull(message = "项目密级不能为空")
    private SecretLevel secretLevel;



}