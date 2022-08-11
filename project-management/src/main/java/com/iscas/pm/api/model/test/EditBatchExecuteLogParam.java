package com.iscas.pm.api.model.test;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.Min;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
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

    @ApiModelProperty(value = "执行记录是否通过", required = true)
    private Boolean pass;


    @ApiModelProperty(value = "测试人员", required = true)
    private String  testPerson;

}
