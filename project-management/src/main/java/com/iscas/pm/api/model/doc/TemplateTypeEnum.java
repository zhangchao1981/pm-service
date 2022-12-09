package com.iscas.pm.api.model.doc;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.Getter;

/**
 * @author by  lichang
 * @date 2022/10/8.
 */
@JsonFormat(shape = JsonFormat.Shape.OBJECT)
@Getter
public enum TemplateTypeEnum {
    SoftwareDevelopment("SoftwareDevelopment", "软件开发计划"),
    DatabaseDesignNotes("DatabaseDesignNotes", "数据库设计说明"),
    QualityAssuranceProgram("QualityAssuranceProgram", "质量保证计划"),
    ConfigurationManagementPlan("ConfigurationManagementPlan", "配置管理计划"),
    SoftwareRequirementsSpecification("SoftwareRequirementsSpecification", "软件需求规格说明"),
    SCTP("SCTP", "配置项测试计划"),
    SCTD("SCTD", "配置项测试说明"),
    SCTR("SCTR", "配置项测试报告");

    private final String code;

    private final String value;

    TemplateTypeEnum(String code, String value) {
        this.code = code;
        this.value = value;
    }
}
