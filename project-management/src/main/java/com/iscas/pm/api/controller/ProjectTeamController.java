package com.iscas.pm.api.controller;

import com.iscas.pm.api.model.project.ProjectUserRole;
import com.iscas.pm.api.model.projectPlan.PlanTask;
import com.iscas.pm.api.service.ProjectTeamService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
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
    @PreAuthorize("hasAuthority('/projectTeam/addMember')")
    public ProjectUserRole addMember(@Valid @RequestBody ProjectUserRole member){
        projectTeamService.save(member);
        return member;
    }

    @GetMapping("/deleteMember")
    @ApiOperation(value = "删除团队成员", notes = "删除指定的团队成员")
    @PreAuthorize("hasAuthority('/projectTeam/deleteMember')")
    public Boolean deleteMember(String id){
        projectTeamService.removeById(id);
        return  true;
    }

}
