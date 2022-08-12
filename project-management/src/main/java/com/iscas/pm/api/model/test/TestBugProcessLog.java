package com.iscas.pm.api.model.test;

import javax.validation.constraints.Size;

import java.io.Serializable;

import java.util.Date;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableName;
import com.iscas.pm.api.model.test.enums.BugProcessActionEnum;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @TableName test_bug_process_log
 */
@Accessors(chain = true)
@ApiModel(value = "测试缺陷处理记录", description = "测试缺陷处理记录，对应test_bug_process_log表")
@TableName(value = "test_bug_process_log")
@Data
public class TestBugProcessLog implements Serializable {
    public TestBugProcessLog(Integer bugId, BugProcessActionEnum action, String description){
        this.action = action;
        this.description = description;
        this.bugId = bugId;
        this.time = new Date();
        this.processor = RequestHolder.getUserInfo().getEmployeeName();
    }

    @TableId(type = IdType.AUTO)
    @ApiModelProperty("id")
    private Integer id;

    @ApiModelProperty("缺陷id")
    private Integer bugId;

    @ApiModelProperty("处理时间")
    private Date time;

    @ApiModelProperty("缺陷状态")
    private BugProcessActionEnum action;

    @Size(max = 20, message = "处理人长度不能超过20")
    @ApiModelProperty("处理人")
    private String processor;

    @ApiModelProperty("处理描述")
    private String description;

}
