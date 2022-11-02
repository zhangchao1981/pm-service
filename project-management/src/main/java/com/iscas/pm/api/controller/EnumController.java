package com.iscas.pm.api.controller;

import com.iscas.pm.api.model.dev.*;
import com.iscas.pm.api.model.doc.DataBaseTypeEnum;
import com.iscas.pm.api.model.doc.DocumentTypeEnum;
import com.iscas.pm.api.model.doc.TemplateTypeEnum;
import com.iscas.pm.api.model.env.MediumEnum;
import com.iscas.pm.api.model.project.ProjectStatusEnum;
import com.iscas.pm.api.model.project.ProjectTypeEnum;
import com.iscas.pm.api.model.project.SecretLevelEnum;
import com.iscas.pm.api.model.projectPlan.TaskStatusEnum;
import com.iscas.pm.api.model.test.enums.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author zhangchao
 * @Date: 20122/7/14 16:42
 * @Description: 枚举类型api
 */
@RestController
@RequestMapping(value = "/enum")
@Api(tags = {"枚举项列表"})
public class EnumController {

    @ApiOperation(value = "项目状态", notes = "获取项目状态枚举项列表")
    @PostMapping(value = "/projectStatus")
    public ProjectStatusEnum[] projectStatusEnums() {
        return ProjectStatusEnum.values();
    }

    @ApiOperation(value = "项目类型", notes = "获取项目类型枚举项列表")
    @PostMapping(value = "/projectType")
    public ProjectTypeEnum[] projectTypeEnums() {
        return ProjectTypeEnum.values();
    }

    @ApiOperation(value = "密级类型", notes = "获取密级类型枚举项列表")
    @PostMapping(value = "/secretLevel")
    public SecretLevelEnum[] secretLevelEnums() {
        return SecretLevelEnum.values();
    }

    @ApiOperation(value = "文档状态", notes = "获取文档状态枚举项列表")
    @PostMapping(value = "/documentType")
    public DocumentTypeEnum[] documentTypeEnums() {
        return DocumentTypeEnum.values();
    }

    @ApiOperation(value = "数据库类型", notes = "查询开发需求或开发任务类型")
    @PostMapping(value = "/dataBaseType")
    public DataBaseTypeEnum[] DateBaseTypeEnum() {
        return DataBaseTypeEnum.values();
    }

    @ApiOperation(value = "介质类型", notes = "获取介质类型枚举项列表")
    @PostMapping(value = "/mediumType")
    public MediumEnum[] mediumTypeEnums() {
        return MediumEnum.values();
    }

    @ApiOperation(value = "需求优先级", notes = "获取需求优先级枚举项列表")
    @PostMapping(value = "/requirementPriority")
    public PriorityEnum[] requirementPriorityEnums() {
        return PriorityEnum.values();
    }

    @ApiOperation(value = "需求来源", notes = "需求开发模块中开发需求的需求来源类型")
    @PostMapping(value = "/requirementSource")
    public RequirementSourceEnum[] RequirementSourceEnum() {
        return RequirementSourceEnum.values();
    }

    @ApiOperation(value = "需求类型", notes = "需求开发模块中开发需求的需求类型")
    @PostMapping(value = "/requirementType")
    public RequirementTypeEnum[] RequirementTypeEnum() {
        return RequirementTypeEnum.values();
    }

    @ApiOperation(value = "需求状态", notes = "获取需求状态枚举项列表")
    @PostMapping(value = "/requirementStatus")
    public RequireStatusEnum[] requirementStatusEnums() {
        return RequireStatusEnum.values();
    }

    @ApiOperation(value = "任务状态", notes = "获取任务状态枚举项列表")
    @PostMapping(value = "/taskStatus")
    public TaskStatusEnum[] taskStatusEnums() {
        return TaskStatusEnum.values();
    }

    @ApiOperation(value = "关联接口类型", notes = "获取关联接口类型列表")
    @PostMapping(value = "/interfaceType")
    public InterfaceTypeEnum[] interfaceTypeEnum() {
        return InterfaceTypeEnum.values();
    }

    @ApiOperation(value = "测试用例等级", notes = "获取测试用例等级列表")
    @PostMapping(value = "/useCaseLevel")
    public UseCasePriorityEnum[] useCaseLevelEnums() {
        return UseCasePriorityEnum.values();
    }

    @ApiOperation(value = "测试用例类型", notes = "获取测试用例类型列表")
    @PostMapping(value = "/useCaseType")
    public UseCaseTypeEnum[] useCaseTypeEnum() {
        return UseCaseTypeEnum.values();
    }

    @ApiOperation(value = "测试类型", notes = "获取测试计划类型列表")
    @PostMapping(value = "/testType")
    public TestTypeEnum[] TestTypeEnum() {
        return TestTypeEnum.values();
    }


    @ApiOperation(value = "缺陷类型", notes = "获取缺陷类型列表")
    @PostMapping(value = "/bugTypeEnums")
    public BugTypeEnum[] bugTypeEnums() {
        return BugTypeEnum.values();
    }

    @ApiOperation(value = "缺陷状态", notes = "获取缺陷状态列表")
    @PostMapping(value = "/bugStatusEnums")
    public BugStatusEnum[] bugStatusEnums() {
        return BugStatusEnum.values();
    }

    @ApiOperation(value = "缺陷来源", notes = "获取缺陷来源列表")
    @PostMapping(value = "/bugSourceEnums")
    public BugSourceEnum[] bugSourceEnums() {
        return BugSourceEnum.values();
    }

    @ApiOperation(value = "缺陷处理结果", notes = "获取测试用例类型列表")
    @PostMapping(value = "/bugSolveResultEnums")
    public BugSolveResultEnum[] bugSolveResultEnums() {
        return BugSolveResultEnum.values();
    }

    @ApiOperation(value = "缺陷严重程度", notes = "获取测试类型列表")
    @PostMapping(value = "/bugSeverityEnums")
    public BugSeverityEnum[] bugSeverityEnums() {
        return BugSeverityEnum.values();
    }

    @ApiOperation(value = "缺陷处理动作", notes = "获取缺陷类型列表")
    @PostMapping(value = "/bugProcessActionEnums")
    public BugProcessActionEnum[] bugProcessActionEnums() {
        return BugProcessActionEnum.values();
    }

    @ApiOperation(value = "缺陷出现频率", notes = "获取缺陷状态列表")
    @PostMapping(value = "/bugProbabilityEnums")
    public BugProbabilityEnum[] bugProbabilityEnums() {
        return BugProbabilityEnum.values();
    }

    @ApiOperation(value = "缺陷注入阶段", notes = "获取缺陷注入阶段列表")
    @PostMapping(value = "/bugInjectStageEnums")
    public BugInjectStageEnum[] bugInjectStageEnums() {
        return BugInjectStageEnum.values();
    }

    @ApiOperation(value = "数据类型", notes = "开发接口数据描述中的数据类型")
    @PostMapping(value = "/dataTypeEnum")
    public DataTypeEnum[] dataTypeEnums() {
        return DataTypeEnum.values();
    }

    @ApiOperation(value = "文档模板类型", notes = "文档模板类型")
    @PostMapping(value = "/templateTypeEnum")
    public TemplateTypeEnum[] templateTypeEnum() {
        return TemplateTypeEnum.values();
    }

}
