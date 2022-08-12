package com.iscas.pm.api.model.test;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @Author： zhangchao
 * @Date： 2022/8/12
 * @Description：
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum BugSolveResultEnum {
    SOLVED("SOLVED", "已解决"),
    SO_DESIGN("SO_DESIGN", "设计如此"),
    NOT_SOLVE("NOT_SOLVE", "不予解决"),
    NO_APPEAR("NO_APPEAR", "无法重现"),
    REPEAT("REPEAT", "重复提交"),
    NOT_BUG("NOT_BUG", "不是缺陷");

    private final String code;
    private final String value;

    BugSolveResultEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
