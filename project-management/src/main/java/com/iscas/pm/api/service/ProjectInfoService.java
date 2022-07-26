package com.iscas.pm.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.project.Project;
import com.iscas.pm.api.model.project.ProjectQo;


import java.util.List;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 项目基本信息管理service接口层
 */
public interface ProjectInfoService extends IService<Project> {

    IPage<Project> projectList(ProjectQo projectQo, Page page);


    List<String> projectPermissions(Project project);

    Project switchProject(String projectId);
}
