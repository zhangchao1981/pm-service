package com.iscas.pm.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iscas.pm.api.model.doc.ProjectDetailInfo;
import com.iscas.pm.api.model.project.*;
import com.iscas.pm.api.service.ProjectInfoService;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 项目基本信息管理控制类
 */
@RestController
@Api(tags = {"项目列表"})
@ApiSort(1)
@RequestMapping("/projectInfo")
public class ProjectInfoController {
    @Autowired
    private ProjectInfoService projectInfoService;

    @PostMapping("/addProject")
    @ApiOperation(value = "添加项目", notes = "添加一个新的项目")
    @PreAuthorize("hasAuthority('/projectInfo/addProject')")
    public Project addProject(@Valid @RequestBody Project project) {
        if (projectInfoService.findProjectByIdAndName(project.getId(), project.getName())) {
            throw new IllegalArgumentException("项目编号或项目名称已经存在！");
        }
        projectInfoService.addProject(project);
        return project;
    }

    @PostMapping("/editProject")
    @ApiOperation(value = "修改项目", notes = "修改处于未关闭状态的项目信息")
    @PreAuthorize("hasAuthority('/projectInfo/editProject')")
    public Project editProject(@RequestBody @Valid Project project) {
        Project dbProject = projectInfoService.getById(project.getId());
        if (dbProject == null)
            throw new IllegalArgumentException("修改的项目不存在");

        if (ProjectStatusEnum.CLOSED == dbProject.getStatus())
            throw new IllegalArgumentException("该项目处于关闭状态,不允许修改");

        if (projectInfoService.findProjectByNotIdAndName(project.getId(), project.getName()))
            throw new IllegalArgumentException("项目名称已经存在！");

        List<String> permissions = projectInfoService.projectPermissions(project.getId());
        //不是本人且没有项目权限，不允许修改
        if (!dbProject.getCreateUser().equals(RequestHolder.getUserInfo().getUserName()) && (permissions == null || !permissions.contains("/projectInfo/editProject")))
            throw new IllegalArgumentException("您无权限修改该项目");
        projectInfoService.saveOrUpdate(project);
        return project;
    }

    @PostMapping("/projectPageList")
    @ApiOperation(value = "项目列表（分页）", notes = "返回符合查询条件且权限范围内的项目列表信息")
    public IPage<Project> projectPageList(@RequestBody @Valid ProjectQueryParam projectQueryParam) {
        //问题： 如果有审批权限 则应当显示全部的项目列表，但这样，切项目的候选栏里就有全部的项目了(尽管有些不能切)

        return projectInfoService.projectPageList(projectQueryParam);
    }

    @GetMapping("/projectList")
    @ApiOperation(value = "项目列表", notes = "返回所有权限范围内的项目列表信息")
    public List<Project> projectList() {
        ProjectQueryParam param = new ProjectQueryParam();
        param.setUserId(RequestHolder.getUserInfo().getId());
        param.setPageNum(1);
        param.setPageSize(Integer.MAX_VALUE);
        return projectInfoService.projectPageList(param).getRecords();
    }

    @PostMapping("/approveProject")
    @ApiOperation(value = "审批项目", notes = "审批通过后，新建项目分库，可以开始其他项目操作")
    @PreAuthorize("hasAuthority('/projectInfo/approveProject')")
    public Boolean approveProject(@RequestBody @Valid Approve approve) {
        Project project = projectInfoService.getById(approve.getId());
        if (project == null)
            throw new IllegalArgumentException("审核的项目不存在");
        if (project.getStatus() != ProjectStatusEnum.CHECK)
            throw new IllegalArgumentException("审核的项目已通过审批，无需审批");

        if (approve.getPass())
            project.setStatus(ProjectStatusEnum.RUNNING);
        else
            project.setStatus(ProjectStatusEnum.UN_PASS);
        project.setApproveComments(approve.getApproveComments());
        return projectInfoService.approveProject(project);
    }

    @PostMapping("/closeProject/{id}")
    @ApiOperation(value = "关闭项目", notes = "关闭指定项目，关闭后只能进行查询操作")
    @PreAuthorize("hasAuthority('/projectInfo/closeProject')")
    public Boolean closeProject(@PathVariable @NotBlank(message = "项目Id不能为空") String id) {
        Project project = projectInfoService.getById(id);
        if (project == null)
            throw new IllegalArgumentException("该项目不存在");
        List<String> permissions = projectInfoService.projectPermissions(project.getId());
        //不是本人且没有项目权限，不允许关闭
        if (!project.getCreateUser().equals(RequestHolder.getUserInfo().getUserName()) && (permissions == null || !permissions.contains("/projectInfo/editProject"))) {
            throw new IllegalArgumentException("您无权限关闭该项目");
        }

        projectInfoService.saveOrUpdate(project.setStatus(ProjectStatusEnum.CLOSED));

        return true;
    }

    @GetMapping("/projectDetailInfo/{id}")
    @ApiOperation(value = "查询项目详情（暂未实现）", notes = "查询指定项目的详细信息，支持前端查询接口")
    @PreAuthorize("hasAuthority('/projectInfo/projectDetailInfo')")
    public ProjectDetailInfo getProjectDetailInfo(@PathVariable @NotBlank(message = "项目Id不能为空") String id) {
        return  projectInfoService.getProjectDetailInfo(id);
    }

    @GetMapping("/switchProject")
    @ApiOperation(value = "切换项目", notes = "根据projectId切换项目")
    public Boolean switchProject(@RequestHeader("Authorization") String token, @NotBlank(message = "projectId不能为空") @RequestParam String projectId) {
        return projectInfoService.switchProject(token, projectId);
    }
}
