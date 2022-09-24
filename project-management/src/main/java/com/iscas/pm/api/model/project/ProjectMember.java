package com.iscas.pm.api.model.project;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * @Author： zhangchao
 * @Date： 2022/9/23
 * @Description：
 */
@Data
public class ProjectMember {

    @ApiModelProperty(value = "人员名称")
    private String employeeName;


    @ApiModelProperty(value = "角色",required = true)
    private String  role;

}
