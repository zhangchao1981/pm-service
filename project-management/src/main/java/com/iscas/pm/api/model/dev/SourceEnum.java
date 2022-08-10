package com.iscas.pm.api.model.dev;

/**
 * @author by  lichang
 * @date 2022/8/9.
 */
public enum SourceEnum {//待修改
    FUNCTION("FUNCTION","功能需求"),
    PERFORMANCE("PERFORMANCE","性能需求"),
    DEPLOY("DEPLOY","部署需求"),
    SECURITY("SECURITY", "安全需求");

    private final String code;
    private final String value;

    SourceEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
