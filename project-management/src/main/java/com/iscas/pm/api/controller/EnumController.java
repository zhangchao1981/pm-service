package com.iscas.pm.api.controller;

import com.iscas.pm.api.model.dev.*;
import com.iscas.pm.api.model.doc.DataBaseTypeEnum;
import com.iscas.pm.api.model.doc.DocumentTypeEnum;
import com.iscas.pm.api.model.env.MediumEnum;
import com.iscas.pm.api.model.project.ProjectStatusEnum;
import com.iscas.pm.api.model.project.ProjectTypeEnum;
import com.iscas.pm.api.model.project.SecretLevelEnum;
import com.iscas.pm.api.model.projectPlan.TaskStatusEnum;
import com.iscas.pm.api.model.test.enums.PriorityEnum;
import com.iscas.pm.api.model.test.enums.UseCaseTypeEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Arrays;

/**
 * @author zhangchao
 * @Date: 20122/7/14 16:42
 * @Description: 枚举类型api
 */
@RestController
@RequestMapping(value = "/enum")
@Api(tags = {"枚举项列表"})
public class EnumController {

    @ApiOperation(value = "需求优先级", notes = "获取需求优先级枚举项列表")
    @PostMapping(value = "/requirementPriority")
    public com.iscas.pm.api.model.dev.PriorityEnum[] requirementPriorityEnums() {
        return com.iscas.pm.api.model.dev.PriorityEnum.values();
    }

    @ApiOperation(value = "需求状态", notes = "获取需求状态枚举项列表")
    @PostMapping(value = "/requirementStatus")
    public RequireStatusEnum[] requirementStatusEnums() {
        return RequireStatusEnum.values();
    }

    @ApiOperation(value = "文档状态", notes = "获取文档状态枚举项列表")
    @PostMapping(value = "/documentType")
    public DocumentTypeEnum[] documentTypeEnums() {
        return DocumentTypeEnum.values();
    }

    @ApiOperation(value = "介质类型", notes = "获取介质类型枚举项列表")
    @PostMapping(value = "/mediumType")
    public MediumEnum[] mediumTypeEnums() {
        return MediumEnum.values();
    }

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

    @ApiOperation(value = "任务状态", notes = "获取任务状态枚举项列表")
    @PostMapping(value = "/taskStatus")
    public TaskStatusEnum[] taskStatusEnums() {
        return TaskStatusEnum.values();
    }

    @ApiOperation(value = "测试用例等级", notes = "获取测试用例等级列表")
    @PostMapping(value = "/useCaseLevel")
    public PriorityEnum[] useCaseLevelEnums() {
        return PriorityEnum.values();
    }

    @ApiOperation(value = "测试用例类型", notes = "获取测试用例类型列表")
    @PostMapping(value = "/useCaseType")
    public UseCaseTypeEnum[] useCaseTypeEnum() {
        return UseCaseTypeEnum.values();
    }


    @ApiOperation(value = "关联接口类型", notes = "获取关联接口类型列表")
    @PostMapping(value = "/interfaceType")
    public InterfaceTypeEnum[] interfaceTypeEnum() {
        return InterfaceTypeEnum.values();
    }

    @ApiOperation(value = "查询开发相关接口的返回值类型", notes = "需求开发模块中项目模块列表、查询开发需求、查询开发任务三个接口的返回值类型")
    @PostMapping(value = "/devType")
    public String[] devTypeEnum() {
        return new String[]{Arrays.toString(DevTypeEnum.values())};
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


    @ApiOperation(value = "查询开发类型", notes = "查询开发需求或开发任务类型")
    @PostMapping(value = "/devQueryType")
    public DevRequirementQueryTypeEnum[] DevRequirementQueryTypeEnum() {
        return DevRequirementQueryTypeEnum.values();
    }


    @ApiOperation(value = "数据库类型", notes = "查询开发需求或开发任务类型")
    @PostMapping(value = "/dataBaseType")
    public DataBaseTypeEnum[] DateBaseTypeEnum() {
        return DataBaseTypeEnum.values();
    }

}
