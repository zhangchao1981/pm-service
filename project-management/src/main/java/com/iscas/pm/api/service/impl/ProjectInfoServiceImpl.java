package com.iscas.pm.api.service.impl;

import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.RolePermissionMapper;
import com.iscas.pm.api.model.project.Project;
import com.iscas.pm.api.mapper.ProjectMapper;
import com.iscas.pm.api.model.project.ProjectQueryParam;
import com.iscas.pm.api.model.project.ProjectUserRole;
import com.iscas.pm.api.service.ProjectInfoService;
import com.iscas.pm.api.service.ProjectUserRoleService;
import com.iscas.pm.common.core.model.AuthConstants;
import com.iscas.pm.common.core.util.RedisUtil;
import com.iscas.pm.common.core.web.exception.AuthorizeException;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import io.netty.util.internal.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletRequest;
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
    private HttpServletRequest request;
    @Autowired
    private ProjectUserRoleService projectUserRoleService;

    @Override
    public IPage<Project> projectList(ProjectQueryParam projectQueryParam) {
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtil.isNullOrEmpty(projectQueryParam.getProjectName()), "name", projectQueryParam.getProjectName());

        Page<Project> page = new Page<>(projectQueryParam.getPageNum(), projectQueryParam.getPageSize());
        IPage projectIPage = projectMapper.selectPage(page, wrapper);

        return projectIPage;
    }

    @Override
    public List<String> projectPermissions(String projectId) {
        Object permissionsJSONString = RequestHolder.getUserInfo().getProjectPermissions().get(projectId);
        List<String> permissionsList = JSONObject.parseObject(JSON.toJSONString(permissionsJSONString), List.class);
        return permissionsList;
    }

    @Override
    public Project switchProject(String projectId) {

        //判断当前用户在要切换的项目上是否有权限
        Integer userId = RequestHolder.getUserInfo().getUserId();
        QueryWrapper<ProjectUserRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("project_id", projectId);
        queryWrapper.eq("user_id", userId);
        if (projectUserRoleService.getOne(queryWrapper) == null) {
            throw new AuthorizeException("当前用户无权限访问目标项目");
        }


        // 有权限则存储到redis里(更新token对应projectid)

        String token = request.getHeader(AuthConstants.AUTHORIZATION_HEADER);
        //请求头里拿到的token有bear 开头
        redisUtil.set(StringUtils.substring(token, 7, token.length()), projectId);


        //返回目标Project信息
        QueryWrapper<Project> projectQuery = new QueryWrapper<>();
        projectQuery.eq("id", projectId);
        return getOne(projectQuery);
    }
}




