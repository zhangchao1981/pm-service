package com.iscas.pm.api.controller;

import com.iscas.pm.api.model.project.ProjectMember;
import com.iscas.pm.api.model.project.ProjectUserRole;
import com.iscas.pm.api.service.ProjectTeamService;
import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author： zhangchao
 * @Date： 2022/8/2
 * @Description： 项目团队控制类
 */
@Api(tags = {"项目团队"})
@RestController
@ApiSort(3)
@RequestMapping("/projectTeam")
public class ProjectTeamController {
    @Autowired
    private ProjectTeamService projectTeamService;


    @PostMapping("/addMember")
    @ApiOperation(value = "批量添加团队成员", notes = "添加多个团队成员，需要传入：userId,roleId")
    @PreAuthorize("hasAuthority('/projectTeam/addMember')")
    public List<ProjectUserRole> addMember(@Valid @RequestBody List<ProjectUserRole> memberList) {
        String projectId = DataSourceHolder.getDB();
        memberList.stream().forEach(member -> {
            member.setEmployeeName(null);
            member.setProjectId(projectId);
        });
        DataSourceHolder.setDB("default");
        try {
            projectTeamService.saveBatch(memberList);
        } catch (Exception e) {
            throw new IllegalArgumentException("人员角色分配重复");
        }
        return memberList;
    }

    @GetMapping("/memberList")
    @ApiOperation(value = "查询团队成员", notes = "查询当前项目下的所有团队成员")
    @PreAuthorize("hasAuthority('/projectTeam/addMember')")
    public List<ProjectUserRole> memberList() {
        return projectTeamService.getMemberList();
    }




    @GetMapping("/deleteMember")
    @ApiOperation(value = "删除团队成员", notes = "删除指定的团队成员")
    @PreAuthorize("hasAuthority('/projectTeam/deleteMember')")
    public Boolean deleteMember(String id) {
        DataSourceHolder.setDB("default");
        if (projectTeamService.getById(id) == null)
            throw new IllegalArgumentException("不存在的人员授权");
        projectTeamService.removeById(id);
        return true;
    }

}
