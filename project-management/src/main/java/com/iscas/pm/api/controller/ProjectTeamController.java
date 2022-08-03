package com.iscas.pm.api.controller;

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
import java.util.List;

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

    @GetMapping("/memberList")
    @ApiOperation(value = "查询团队成员", notes = "查询所有团队成员")
    public List<ProjectUserRole> memberList(){
        return  projectTeamService.getMemberList();
    }

    @PostMapping("/addMember")
    @ApiOperation(value = "添加团队成员", notes = "添加一个团队成员")
    @PreAuthorize("hasAuthority('/projectTeam/memberManage')")
    public ProjectUserRole addMember(@Valid @RequestBody ProjectUserRole member){
        String projectId = DataSourceHolder.getDB();
        DataSourceHolder.setDB("default");

        member.setProjectId(projectId);
        member.setEmployeeName(null);
        projectTeamService.save(member);
        return member;
    }

    @GetMapping("/deleteMember")
    @ApiOperation(value = "删除团队成员", notes = "删除指定的团队成员")
    @PreAuthorize("hasAuthority('/projectTeam/memberManage')")
    public Boolean deleteMember(String id){
        DataSourceHolder.setDB("default");
        if (projectTeamService.getById(id) == null)
            throw new IllegalArgumentException("不存在的人员授权");

        projectTeamService.removeById(id);
        return  true;
    }

}
