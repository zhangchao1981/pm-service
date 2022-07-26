package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.RolePermissionMapper;
import com.iscas.pm.api.model.project.Project;
import com.iscas.pm.api.mapper.ProjectMapper;
import com.iscas.pm.api.model.project.ProjectQo;
import com.iscas.pm.api.service.ProjectInfoService;
import io.netty.util.internal.StringUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.context.request.RequestContextHolder;

import java.util.List;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 项目基本信息管理service实现类
 */
@Service
public class ProjectInfoServiceImpl extends ServiceImpl<ProjectMapper,Project> implements ProjectInfoService{
    @Autowired
    private ProjectMapper projectMapper;
    @Autowired
    RolePermissionMapper  permissionMapper;
    @Override
    public IPage<Project>  projectList(ProjectQo projectQo,Page page) {
        QueryWrapper<Project> wrapper = new QueryWrapper<>();
        //拿到用户id--->找到该用户隶属的项目
            wrapper.like(!StringUtil.isNullOrEmpty(projectQo.getProjectName()),"name",projectQo.getProjectName());
            wrapper.eq(!StringUtil.isNullOrEmpty(projectQo.getStatus().getCode()),"status",projectQo.getStatus());

        //传入的无论是1还是clossed 都会被转化为closed
        //输出的
        IPage projectIPage = projectMapper.selectPage(page, wrapper);
        projectIPage.setTotal(projectMapper.selectCount(wrapper));
        return projectIPage;
    }

    @Override
    public List<String> projectPermissions(Project project) {
        Object userid = RequestContextHolder.getRequestAttributes().getAttribute("userid", 0);
        //拿到userid 和 projectid  判断用户是否在该project里面是有编辑权限的角色
        String projectId = project.getId();


        return permissionMapper.getPerMissions((Integer) userid, Integer.parseInt(projectId));
    }
}




