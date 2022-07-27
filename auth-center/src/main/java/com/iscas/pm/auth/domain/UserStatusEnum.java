package com.iscas.pm.auth.domain;

/**
 * @Author： zhangchao
 * @Date： 2022/7/19
 * @Description： 用户状态枚举类
 */
public enum UserStatusEnum {
    NORMAL("NORMAL","正常"),
    CANCEL("CANCEL","已注销");

    private final String code;
    private final String value;

    UserStatusEnum(String code,String value) {
        this.code = code;
        this.value = value;
    }
}
