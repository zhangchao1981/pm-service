package com.iscas.pm.api.model.dev;

/**
 * 查询开发需求或开发任务类型
 *
 * @author by  lichang
 * @date 2022/10/9.
 */
public enum DevRequirementQueryTypeEnum {

    DevRequirement("DevRequirement", "开发需求"),
    DevTask("DevTask", "开发任务");

    private final String code;
    private final String value;

    DevRequirementQueryTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }

}
