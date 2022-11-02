package com.iscas.pm.api.model.test.param;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import javax.validation.constraints.Size;
import java.util.List;

/**
 * @author by  lichang
 * @date 2022/8/11.
 */
@Data
public class EditBatchExecuteLogParam {
    @ApiModelProperty(value = "待修改执行记录的idList", required = true)
    @NotEmpty(message = "idList不能为空")
    private List<Integer> idList;

    @ApiModelProperty(value = "执行记录是否通过(更改通过状态接口需要)", required = true)
    private Boolean pass;

    @ApiModelProperty(value = "测试人员雇员名(指定测试人员接口需要)", required = true)
    @Size(max = 20, message = "测试人员雇员名长度不能超过20")
    private String  testPerson;

    @ApiModelProperty(value = "测试人员的用户id(指定测试人员接口需要)", required = true)
    private Integer  testPersonId;

}
