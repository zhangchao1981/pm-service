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
    @PreAuthorize("hasAuthority('/projectTeam/memberManage')")
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
    public List<ProjectUserRole> memberList() {
        //问题：返回雇员名可能重名，需要用户名    方案1.
        return projectTeamService.getMemberList();
    }

//    @GetMapping("/memberListByRole")
//    @ApiOperation(value = "查询某角色对应的团队成员", notes = "查询团队成员中指定角色的成员,用于回显")//未添加权限
//    public List<ProjectUserRole> memberListByRole(@NotNull(message = "角色ID不能为空") @RequestParam Integer roleId ){
//        return  projectTeamService.getMemberByRole(roleId);
//    }
//
////    @GetMapping("/roleList")
////    @ApiOperation(value = "查询团队角色列表(弃用)", notes = "查询当前项目下的所有角色")
////    public List<String> roleList(){
////        return  projectTeamService.getRoleList();
////    }

    @GetMapping("/deleteMember")
    @ApiOperation(value = "删除团队成员", notes = "删除指定的团队成员")
    @PreAuthorize("hasAuthority('/projectTeam/memberManage')")
    public Boolean deleteMember(String id) {
        DataSourceHolder.setDB("default");
        if (projectTeamService.getById(id) == null)
            throw new IllegalArgumentException("不存在的人员授权");
        projectTeamService.removeById(id);
        return true;
    }

}
