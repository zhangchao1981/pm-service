package com.iscas.pm.api.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iscas.pm.api.model.project.*;
import com.iscas.pm.api.service.ProjectInfoService;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 项目基本信息管理控制类
 */
@RestController
@Api(tags = {"项目基本信息"})
@RequestMapping("/projectInfo")
public class ProjectInfoController {
    @Autowired
    private ProjectInfoService projectInfoService;

    @PostMapping("/addProject")
    @ApiOperation(value = "添加项目", notes = "添加一个新的项目")
    @PreAuthorize("hasAuthority('/projectInfo/addProject')")
    public Project addProject(@Valid @RequestBody Project project) {

        project.setCreateUser(RequestHolder.getUserInfo().getUsername());
        project.setStatus(ProjectStatusEnum.RUNNING);
        project.setCreateTime(new Date());
        project.setUpdateTime(new Date());

        projectInfoService.save(project);
        return project;
    }

    @PostMapping("/editProject")
    @ApiOperation(value = "修改项目", notes = "修改处于未关闭状态的项目信息")
    @PreAuthorize("hasAuthority('/projectInfo/editProject')")
    public Project editProject(@RequestBody @Valid Project project) {
        Project db_project = projectInfoService.getById(project.getId());
        if (db_project == null)
            throw new IllegalArgumentException("修改的项目不存在");

        if(ProjectStatusEnum.CLOSED==db_project.getStatus())
            throw new IllegalArgumentException("该项目处于关闭状态,不允许修改");

        if (!projectInfoService.projectPermissions(project.getId()).contains("/projectInfo/editProject"))
            throw new  IllegalArgumentException("您无权限修改该项目");

        projectInfoService.saveOrUpdate(project);

        return project;
    }

    @PostMapping("/projectList")
    @ApiOperation(value = "项目列表", notes = "返回符合查询条件且权限范围内的项目列表信息")
    public IPage<Project> projectList(@RequestBody @Valid ProjectQueryParam projectQueryParam) {
        return  projectInfoService.projectList(projectQueryParam);
    }

    @PostMapping("/approveProject")
    @ApiOperation(value = "审批项目", notes = "审批通过后，新建项目分库，可以开始其他项目操作")
    //    @PreAuthorize("hasAuthority('/projectInfo/approveProject')")
    public Project approveProject(@RequestBody @Valid  Audit audit) {
        //点击审批项目，用户开始审批(拿到project信息)
        Project project = projectInfoService.getById(audit.getId());
        //审批完成，通过审核(或不通过)，更改project的status
        project.setStatus(ProjectStatusEnum.RUNNING);
        //将审核结果通知申请人
        //更新project表
        projectInfoService.saveOrUpdate(project);
        return project;
    }

    @PostMapping("/closeProject/{id}")
    @ApiOperation(value = "关闭项目", notes = "关闭指定项目，关闭后只能进行查询操作")
//    @PreAuthorize("hasAuthority('/projectInfo/closeProject')")
    public Boolean closeProject(@PathVariable @NotBlank(message = "项目Id不能为空") String id) {
       Project project = projectInfoService.getById(id);
        if (project==null){
            throw new IllegalArgumentException("未查询到指定项目");
        }
        //然后判断是否有权限
        if (projectInfoService.projectPermissions(project.getId()).contains("/projectInfo/editProject")) {
            return projectInfoService.saveOrUpdate(project.setStatus(ProjectStatusEnum.CLOSED));
        }
        return false;
    }

    /**
     * 这个详细信息还没写
     * @param id
     * @return
     */
    @GetMapping("/projectDetailInfo/{id}")
    @ApiOperation(value = "查询项目详情", notes = "查询指定项目的详细信息，支持前端查询接口")
//    @PreAuthorize("hasAuthority('/projectInfo/projectDetailInfo')")
    public ProjectDetailInfo getProjectDetailInfoById(@PathVariable @NotBlank String id) {
        Project project = projectInfoService.getById(id);
        ProjectDetailInfo detailInfo = new ProjectDetailInfo();
        //填入哪些额外信息
        return detailInfo;
    }

    @GetMapping("/switchProject")
    @ApiOperation(value = "切换项目", notes = "根据projectId切换项目，projectId为空时，切换到第一个项目上")
    public Project switchProject(@NotBlank(message = "projectId不能为空") @RequestParam String projectId) {
        return projectInfoService.switchProject(projectId);
        //判断是否有指定项目的权限
        //从 pm_project_user_role表里面找到userid-projectid对应的role
    }





}
