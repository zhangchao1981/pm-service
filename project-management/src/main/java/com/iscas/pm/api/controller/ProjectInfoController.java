package com.iscas.pm.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iscas.pm.api.model.project.*;
import com.iscas.pm.api.service.ProjectInfoService;
import com.iscas.pm.api.service.ProjectUserRoleService;
import com.iscas.pm.api.service.RolePermissionService;
import com.iscas.pm.common.core.web.exception.SimpleBaseException;
import io.netty.util.internal.StringUtil;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import javax.management.relation.Role;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Enumeration;
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
    @Autowired
    private ProjectUserRoleService  projectUserRoleService;
    @Autowired
    private RolePermissionService rolePermissionService;

    @PostMapping("/addProject")
    @ApiOperation(value = "申请立项", notes = "申请建立一个新的项目")
//    @PreAuthorize("hasAuthority('/projectInfo/addProject')")
    public Project addProject(@Valid @RequestBody Project project) {
        projectInfoService.save(project);
        return project;
    }

    @PostMapping("/editProject")
    @ApiOperation(value = "修改项目", notes = "修改处于未关闭状态的项目信息")
//    @PreAuthorize("hasAuthority('/projectInfo/editProject')")
    public Project editProject(@RequestBody Project project) {
        projectInfoService.saveOrUpdate(project);
        return project;
    }

    @GetMapping("/projectList")
    @ApiOperation(value = "项目列表", notes = "返回符合查询条件且权限范围内的项目列表信息")
//    @PreAuthorize("hasAuthority('/projectInfo/projectList')")
    public List<Project> projectList(@RequestBody ProjectQo projectQo) {
        return  projectInfoService.projectList(projectQo);

    }

    @PostMapping("/approveProject")
    @ApiOperation(value = "审批项目", notes = "审批通过后，新建项目分库，可以开始其他项目操作")
//    @PreAuthorize("hasAuthority('/projectInfo/approveProject')")
    public Project approveProject(@RequestBody Audit audit) {
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
    public Boolean closeProject(@PathVariable String id) {
       Project project = projectInfoService.getById(id);
        if (project==null){
            throw new SimpleBaseException(404,"未查询到指定项目");
        }
        projectInfoService.saveOrUpdate(project.setStatus(ProjectStatusEnum.CLOSED));
        return null;
    }

    /**
     * 这个详细信息还没写
     * @param id
     * @return
     */
    @GetMapping("/projectDetailInfo/{id}")
    @ApiOperation(value = "查询项目详情", notes = "查询指定项目的详细信息，支持前端查询接口")
//    @PreAuthorize("hasAuthority('/projectInfo/projectDetailInfo')")
    public ProjectDetailInfo getProjectDetailInfoById(@PathVariable String id) {
        Project project = projectInfoService.getById(id);
        ProjectDetailInfo detailInfo = new ProjectDetailInfo();
        //填入哪些额外信息
        return detailInfo;
    }




    @GetMapping("/switchProject")
    @ApiOperation(value = "切换项目", notes = "根据projectId切换项目，projectId为空时，切换到第一个项目上")
    public Project switchProject(@NotBlank(message = "projectId不同为空") @RequestParam Integer projectId) {
        //判断是否有指定项目的权限
        //从 pm_project_user_role表里面找到userid-projectid对应的role
        //从请求头里获取userid
//        SecurityContextHolder.getContext().getAuthentication().getDetails().getPrincipal().get("UserId");
        Integer userId = new Integer(1);
        QueryWrapper<ProjectUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id",projectId);
        queryWrapper.eq("user_id",userId);
        ProjectUserRole roleInfo = projectUserRoleService.getOne(queryWrapper);
        if (roleInfo.getRoleId()==null){
            //没有找到对应的角色，判断为无权限
            return null;
        }
        // 有权限则查询该角色的权限列表
       List<String> permissions=rolePermissionService.getPermissions(roleInfo.getRoleId());
        //在redis中存储accesstoken 或jti与projectid的映射关系

        //返回对应Project信息
        QueryWrapper<Project> projectQuery = new QueryWrapper<>();
        projectQuery.eq("id",projectId);
        return  projectInfoService.getOne(projectQuery);
    }
}
