package com.iscas.pm.api.model.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/8/11
 * @Description： bug状态枚举类
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum BugProcessActionEnum {

    NEW("NEW", "新建"),
    EDIT("EDIT", "便捷"),
    START("START", "开始解决"),
    SOLVED("SOLVED", "已解决"),
    TRANSFER("TRANSFER", "转办"),
    FEEDBACK("FEEDBACK", "反馈"),
    DELAYED_SOLVED("DELAYED_SOLVED", "延时解决"),
    CLOSE("CLOSE", "关闭"),
    REOPEN("REOPEN", "重新打开");

    private final String code;
    private final String value;

    BugProcessActionEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
