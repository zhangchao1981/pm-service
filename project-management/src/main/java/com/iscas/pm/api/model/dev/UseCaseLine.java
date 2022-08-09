package com.iscas.pm.api.model.dev;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @Author： zhangchao
 * @Date： 2022/8/9
 * @Description：
 */
@Data
@ApiModel(value = "需求用例表中的行记录")
public class UseCaseLine {
    @ApiModelProperty("行编号")
    private String number;

    @ApiModelProperty("行描述")
    private String description;
}
