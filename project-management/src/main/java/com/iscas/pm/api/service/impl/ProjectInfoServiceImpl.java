package com.iscas.pm.api.service.impl;

import com.iscas.pm.api.model.project.Project;
import com.iscas.pm.api.mapper.ProjectMapper;
import com.iscas.pm.api.service.ProjectInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 项目基本信息管理service实现类
 */
@Service
public class ProjectInfoServiceImpl  implements ProjectInfoService {
    @Resource
    private ProjectMapper projectMapper;

    @Override
    public void addProject(Project project) {
        projectMapper.addProject(project);
    }
}




