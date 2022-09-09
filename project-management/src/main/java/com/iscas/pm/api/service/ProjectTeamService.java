package com.iscas.pm.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.project.ProjectUserRole;

import javax.validation.constraints.NotNull;
import java.util.List;

/**
* @author zhangchao
* @description 针对表【pm_project_user_role】的数据库操作Service
* @createDate 2022-07-28 17:26:03
*/
public interface ProjectTeamService extends IService<ProjectUserRole> {

    List<ProjectUserRole> getMemberList();

//    List<ProjectUserRole> getMemberByRole(@NotNull(message = "角色ID不能为空") Integer roleId);
//
//    List<String> getRoleList();
}
