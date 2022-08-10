package com.iscas.pm.api.model.test;

import javax.validation.constraints.Size;

import java.io.Serializable;

import java.util.Date;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
* 
* @TableName test_bug_process_log
*/
@Data
public class TestBugProcessLog implements Serializable {

    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("缺陷id")
    private Integer bugId;

    @ApiModelProperty("处理时间")
    private Date time;

    @ApiModelProperty("处理动作")
    private String action;

    @Size(max= 20,message="处理人长度不能超过20")
    @ApiModelProperty("处理人")
    private String processor;

    @ApiModelProperty("处理描述")
    private String description;

}
