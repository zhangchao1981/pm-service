package com.iscas.pm.api.model.dev;

import com.baomidou.mybatisplus.annotation.TableField;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import org.codehaus.jackson.annotate.JsonIgnore;

import java.util.List;

/**
 * @author by  lichang
 * @date 2022/8/4.
 */

@Data
public class UseCase {
    @ApiModelProperty(value = "需求角色")
    private String  role;

    @ApiModelProperty(value = "前置条件")
    private List<String>  precondition;

    @ApiModelProperty(value = "成功场景")
    private List<String> successScene;

    @ApiModelProperty(value = "分支场景")
    private List<String> branchScene;

    @ApiModelProperty(value = "业务规则设计约束")
    private List<String> constraint;

}
