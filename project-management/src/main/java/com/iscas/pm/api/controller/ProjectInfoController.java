package com.iscas.pm.api.controller;

import com.iscas.pm.api.model.project.Project;
import com.iscas.pm.api.model.project.ProjectQo;
import com.iscas.pm.api.model.project.ProjectDetailInfo;
import com.iscas.pm.api.service.ProjectInfoService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 项目基本信息管理
 */
@RestController
@Api(tags = {"项目基本信息"})
@RequestMapping("/projectInfo")
public class ProjectInfoController {
    @Autowired
    private ProjectInfoService projectInfoService;

    @PostMapping("/addProject")
    @ApiOperation(value = "申请立项", notes = "申请建立一个新的项目")
    @PreAuthorize("hasAuthority('/projectInfo/addProject')")
    public Project addProject(@Valid @RequestBody Project project) {
        projectInfoService.save(project);
        return project;
    }

    @PostMapping("/editProject")
    @ApiOperation(value = "修改项目", notes = "修改处于未关闭状态的项目信息")
    @PreAuthorize("hasAuthority('/projectInfo/editProject')")
    public Project editProject(@RequestBody Project project) {

        return project;
    }

    @PostMapping("/projectList")
    @ApiOperation(value = "项目列表", notes = "返回符合查询条件且权限范围内的项目列表信息")
    @PreAuthorize("hasAuthority('/projectInfo/projectList')")
    public List<Project> projectList(@RequestBody ProjectQo projectQo) {
        return projectInfoService.get();
    }

    @PostMapping("/approveProject")
    @ApiOperation(value = "审批项目", notes = "审批通过后，新建项目分库，可以开始其他项目操作")
    @PreAuthorize("hasAuthority('/projectInfo/approveProject')")
    public Project approveProject(@RequestBody Project project) {

        return null;
    }

    @PostMapping("/closeProject/{id}")
    @ApiOperation(value = "关闭项目", notes = "关闭指定项目，关闭后只能进行查询操作")
    @PreAuthorize("hasAuthority('/projectInfo/closeProject')")
    public Boolean closeProject(@PathVariable String id) {

        return null;
    }

    @GetMapping("/projectDetailInfo/{id}")
    @ApiOperation(value = "查询项目详情", notes = "查询指定项目的详细信息，支持前端查询接口")
    @PreAuthorize("hasAuthority('/projectInfo/projectDetailInfo')")
    public ProjectDetailInfo getProjectDetailInfoById(@PathVariable String id) {

        return null;
    }

    @GetMapping("/switchProject")
    @ApiOperation(value = "切换项目", notes = "根据projectId切换项目，projectId为空时，切换到第一个项目上")
    public Project switchProject(@NotBlank(message = "projectId不同为空") @RequestParam String projectId) {

        return null;
    }




}
