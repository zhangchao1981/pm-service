package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.project.ProjectUserRoleMapper;
import com.iscas.pm.api.model.doc.ProjectDetailInfo;
import com.iscas.pm.api.model.project.*;
import com.iscas.pm.api.mapper.project.ProjectMapper;
import com.iscas.pm.api.service.InitSchemaService;
import com.iscas.pm.api.service.ProjectInfoService;
import com.iscas.pm.common.core.model.UserInfo;
import com.iscas.pm.common.core.util.RedisUtil;
import com.iscas.pm.common.core.web.exception.AuthorizeException;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 项目基本信息管理service实现类
 */
@Service
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectMapper, Project> implements ProjectInfoService {
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private InitSchemaService initSchemaService;
    @Autowired
    private ProjectUserRoleMapper projectUserRoleMapper;
    @Autowired
    UserService userService;

    @Override
    public IPage<Project> projectPageList(ProjectQueryParam param) {
        Page<Project> page = new Page<>(param.getPageNum(), param.getPageSize());
        List<String> systemPermissions = RequestHolder.getUserInfo().getSystemPermissions();
        if (systemPermissions != null)
            if (systemPermissions.contains("/projectInfo/approveProject")) {
                QueryWrapper<Project> queryWrapper = new QueryWrapper<Project>()
                        .eq(StringUtils.isNotBlank(param.getStatus()), "status", param.getStatus())
                        .like(StringUtils.isNotBlank(param.getProjectName()), "name", param.getProjectName());
                return projectMapper.selectPage(page, queryWrapper);
            }
        //否则返回有权限的项目
        param.setUserId(RequestHolder.getUserInfo().getId());
        return projectMapper.getProjectList(page, param);
    }

    @Override
    public List<String> projectPermissions(String projectId) {
        return RequestHolder.getUserInfo().getProjectPermissions().get(projectId);
    }

    @Override
    public Boolean switchProject(String token, String projectId) {
        //超级角色可以切换到任意项目
        List<String> systemPermissions = RequestHolder.getUserInfo().getSystemPermissions();
        boolean super_role = false;
        if (systemPermissions != null && systemPermissions.contains("/projectInfo/addProject") )
            super_role = true;

        //其他用户只能切换到有权限的项目
        Map<String, List<String>> projectPermissions = RequestHolder.getUserInfo().getProjectPermissions();
        if (!super_role && (projectPermissions == null || projectPermissions.get(projectId) == null || projectPermissions.get(projectId).size() == 0)) {
            throw new AuthorizeException("当前用户无权限访问目标项目");
        }
        //redis中更新当前项目
        UserInfo userInfo = RequestHolder.getUserInfo();
        userInfo.setCurrentProjectId(projectId);
        redisUtil.hset(RequestHolder.getUserInfo().getId().toString(), StringUtils.substring(token, 7, token.length()), userInfo);
        return true;
    }

    @Override
    public Project addProject(Project project) {
        project.setCreateUser(RequestHolder.getUserInfo().getUserName());
        project.setStatus(ProjectStatusEnum.CHECK);
        project.setCreateTime(new Date());
        project.setUpdateTime(new Date());

        projectMapper.insert(project);

        //为创建者添加项目经理角色
        ProjectUserRole member = new ProjectUserRole();
        member.setProjectId(project.getId());
        member.setUserId(RequestHolder.getUserInfo().getId());
        member.setRoleId(6);
        projectUserRoleMapper.insert(member);
        return project;
    }

    @Override
    public Boolean findProjectByIdAndName(String id, String name) {
        List<Project> project = projectMapper.findProjectByIdAndName(id, name);
        if (project == null || project.size() == 0)
            return false;
        return true;
    }

    @Override
    public Boolean findProjectByNotIdAndName(String id, String name) {
        List<Project> project = projectMapper.findProjectByNotIdAndName(id, name);
        if (project == null || project.size() == 0)
            return false;
        return true;
    }

    @Override
    public Boolean approveProject(Project project) {
        //审批结果存库
        projectMapper.updateById(project);

        //审批通过，初始化mysql项目分库
        if (project.getStatus() == ProjectStatusEnum.RUNNING) {
            initSchemaService.initSchema(project.getId());
        }

        //分配权限
        SettingSystemRoleQueryParam roleQueryParam = new SettingSystemRoleQueryParam();
        roleQueryParam.setUserId(project.getUserId());
        roleQueryParam.setRoles(Arrays.asList(6));
        return  userService.settingSystemRole(roleQueryParam);
    }

    @Override
    public ProjectDetailInfo getProjectDetailInfo(String id) {
        Project project = projectMapper.selectById(id);
        if (project == null) {
            throw new IllegalArgumentException("该项目不存在");
        }
        ProjectDetailInfo projectDetailInfo = new ProjectDetailInfo();
        projectDetailInfo.setBasicInfo(project);
        //待补充其余项目详情信息

        return projectDetailInfo;
    }
}




