package com.iscas.pm.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.project.Project;
import com.iscas.pm.api.model.project.ProjectQueryParam;


import java.util.List;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 项目基本信息管理service接口层
 */
public interface ProjectInfoService extends IService<Project> {

    IPage<Project> projectList(ProjectQueryParam projectQueryParam);


    List<String> projectPermissions(String projectId);

    Project switchProject(String projectId);
}
