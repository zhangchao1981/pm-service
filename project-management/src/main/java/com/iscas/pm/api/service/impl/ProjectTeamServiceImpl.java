package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.project.ProjectUserRoleMapper;
import com.iscas.pm.api.model.project.ProjectMember;
import com.iscas.pm.api.model.project.ProjectUserRole;
import com.iscas.pm.api.service.ProjectTeamService;
import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zhangchao
* @description 针对表【pm_project_user_role】的数据库操作Service实现
* @createDate 2022-07-28 17:26:03
*/
@Service
public class ProjectTeamServiceImpl extends ServiceImpl<ProjectUserRoleMapper, ProjectUserRole> implements ProjectTeamService {
    @Autowired
    private ProjectUserRoleMapper projectUserRoleMapper;

    @Override
    public List<ProjectUserRole> getMemberList() {
        String projectId = DataSourceHolder.getDB().databaseName;
        DataSourceHolder.setDB("default");
        return  projectUserRoleMapper.getMemberList(projectId);
    }

    @Override
    public List<ProjectMember> memberRoleList(String projectId) {
        return  projectUserRoleMapper.memberRoleList(projectId);
    }

}




