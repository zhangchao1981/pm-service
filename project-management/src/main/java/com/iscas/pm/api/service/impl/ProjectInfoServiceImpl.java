package com.iscas.pm.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.ProjectUserRoleMapper;
import com.iscas.pm.api.model.project.*;
import com.iscas.pm.api.mapper.ProjectMapper;
import com.iscas.pm.api.service.InitSchemaService;
import com.iscas.pm.api.service.ProjectInfoService;
import com.iscas.pm.common.core.util.RedisUtil;
import com.iscas.pm.common.core.web.exception.AuthorizeException;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

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
    private ProjectUserRoleMapper projectUserRoleMapper;
    @Autowired
    private InitSchemaService initSchemaService;

    @Override
    public IPage<Project> projectList(ProjectQueryParam param) {
        Page<Project> page = new Page<>(param.getPageNum(), param.getPageSize());
        param.setUserId(RequestHolder.getUserInfo().getUserId());
        IPage projectIPage = projectMapper.getProjectList(page, param);

        return projectIPage;
    }

    @Override
    public List<String> projectPermissions(String projectId) {
        Object permissionsJSONString = RequestHolder.getUserInfo().getProjectPermissions().get(projectId);
        List<String> permissionsList = JSONObject.parseObject(JSON.toJSONString(permissionsJSONString), List.class);
        return permissionsList;
    }

    @Override
    public Boolean switchProject(String token,String projectId) {
        //判断当前用户在要切换的项目上是否有权限

        Integer userId = RequestHolder.getUserInfo().getUserId();
        QueryWrapper<ProjectUserRole> queryWrapper = new QueryWrapper<>();
//两种方案  1. 在这里截断 project_项目名   2.在放的地方
        queryWrapper.eq("project_id", projectId);
        queryWrapper.eq("user_id", userId);
        if (projectUserRoleMapper.selectOne(queryWrapper) == null) {
            throw new AuthorizeException("当前用户无权限访问目标项目");
        }

        //redis中更新当前项目
        redisUtil.set(StringUtils.substring(token, 7, token.length()), projectId);
        return true;
    }

    @Override
    public Project addProject(Project project) {
        project.setCreateUser(RequestHolder.getUserInfo().getUsername());
        project.setStatus(ProjectStatusEnum.CHECK);
        project.setCreateTime(new Date());
        project.setUpdateTime(new Date());

        projectMapper.insert(project);
        return project;
    }

    @Override
    public Boolean findProjectByIdAndName(String id, String name) {
        Project project = projectMapper.findProjectByIdAndName(id, name);
        if (project == null)
            return false;
        return true;
    }

    @Override
    public Boolean findProjectByNotIdAndName(String id, String name) {
        Project project = projectMapper.findProjectByNotIdAndName(id, name);
        if (project == null)
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
        return true;
    }
}




