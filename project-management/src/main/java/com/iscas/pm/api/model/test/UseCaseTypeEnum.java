package com.iscas.pm.api.model.test;

/**
 * 测试用例类型
 * @author by  lichang
 * @date 2022/8/10.
 */
public enum UseCaseTypeEnum {

    FUNCTION_TEST("FUNCTION", "功能测试"),
    PERFORMANCE_TEST("PERFORMANCE", "性能测试"),
    INSTALL_DEPLOYMENT("INSTALL_DEPLOYMENT", "安装部署");

    private final String code;
    private final String value;

    UseCaseTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
