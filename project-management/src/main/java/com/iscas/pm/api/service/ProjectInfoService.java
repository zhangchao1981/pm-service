package com.iscas.pm.api.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.doc.ProjectDetailInfo;
import com.iscas.pm.api.model.project.Project;
import com.iscas.pm.api.model.project.ProjectQueryParam;


import java.util.List;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 项目基本信息管理service接口层
 */
public interface ProjectInfoService extends IService<Project> {

    IPage<Project> projectPageList(ProjectQueryParam projectQueryParam);

    List<Project> projectList();

    List<String> projectPermissions(String projectId);

    Boolean switchProject(String token,String projectId);

    Project addProject(Project project);

    Boolean findProjectByIdAndName(String id, String name);

    Boolean findProjectByNotIdAndName(String id, String name);

    Boolean approveProject(Project project);

    ProjectDetailInfo getProjectDetailInfo(String id);
}
