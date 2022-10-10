package com.iscas.pm.api.model.test.enums;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/8/11
 * @Description： bug处理动作枚举类
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum BugProcessActionEnum {

    NEW("NEW", "新建缺陷"),
    EDIT("EDIT", "编辑缺陷"),
    START("START", "开始解决缺陷"),
    SOLVED("SOLVED", "解决缺陷"),
    TRANSFER("TRANSFER", "转办缺陷"),
    DISPATCH("DISPATCH", "指派缺陷"),
    FEEDBACK("FEEDBACK", "填写反馈"),
    DELAYED_SOLVED("DELAYED_SOLVED", "延时解决缺陷"),
    CLOSE("CLOSE", "关闭缺陷"),
    REOPEN("REOPEN", "重新打开缺陷");

    private final String code;
    private final String value;

    BugProcessActionEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
