package com.iscas.pm.api.controller;

import com.iscas.pm.api.model.dev.PriorityEnum;
import com.iscas.pm.api.model.dev.RequireStatusEnum;
import com.iscas.pm.api.model.doc.DocumentTypeEnum;
import com.iscas.pm.api.model.env.MediumEnum;
import com.iscas.pm.api.model.project.ProjectStatusEnum;
import com.iscas.pm.api.model.project.ProjectTypeEnum;
import com.iscas.pm.api.model.project.SecretLevelEnum;
import com.iscas.pm.api.model.projectPlan.TaskStatusEnum;
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

    @ApiOperation(value = "需求优先级",notes = "获取需求优先级枚举项列表")
    @PostMapping(value = "/requirementPriority")
    public PriorityEnum[] requirementPriorityEnums() {
        return PriorityEnum.values();
    }

    @ApiOperation(value = "需求状态",notes = "获取需求状态枚举项列表")
    @PostMapping(value = "/requirementStatus")
    public RequireStatusEnum[] requirementStatusEnums() {
        return RequireStatusEnum.values();
    }

    @ApiOperation(value = "文档状态",notes = "获取文档状态枚举项列表")
    @PostMapping(value = "/documentType")
    public DocumentTypeEnum[] documentTypeEnums() {
        return DocumentTypeEnum.values();
    }

    @ApiOperation(value = "介质类型",notes = "获取介质类型枚举项列表")
    @PostMapping(value = "/mediumType")
    public MediumEnum[] mediumTypeEnums() {
        return MediumEnum.values();
    }

    @ApiOperation(value = "项目状态",notes = "获取项目状态枚举项列表")
    @PostMapping(value = "/projectStatus")
    public ProjectStatusEnum[] projectStatusEnums() {
        return ProjectStatusEnum.values();
    }

    @ApiOperation(value = "项目类型",notes = "获取项目类型枚举项列表")
    @PostMapping(value = "/projectType")
    public ProjectTypeEnum[] projectTypeEnums() {
        return ProjectTypeEnum.values();
    }

    @ApiOperation(value = "密级类型",notes = "获取密级类型枚举项列表")
    @PostMapping(value = "/secretLevel")
    public SecretLevelEnum[] secretLevelEnums() {
        return SecretLevelEnum.values();
    }

    @ApiOperation(value = "任务状态",notes = "获取任务状态枚举项列表")
    @PostMapping(value = "/taskStatus")
    public TaskStatusEnum[] taskStatusEnums() {
        return TaskStatusEnum.values();
    }

}
