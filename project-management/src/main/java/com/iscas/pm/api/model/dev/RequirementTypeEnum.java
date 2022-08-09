package com.iscas.pm.api.model.dev;

/**
 * @author by  lichang
 * @date 2022/8/9.
 * @D
 */
public enum RequirementTypeEnum {
    FUNCTION("FUNCTION","功能需求"),
    PERFORMANCE("PERFORMANCE","性能需求"),
    DEPLOY("DEPLOY","部署需求"),
    SECURITY("SECURITY", "安全需求");

    private final String code;
    private final String value;

    RequirementTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
