package com.iscas.pm.api.service.impl;

import com.iscas.pm.api.model.project.Project;
import com.iscas.pm.api.mapper.ProjectMapper;
import com.iscas.pm.api.service.ProjectInfoService;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
* @author 66410
* @description 针对表【pjdata】的数据库操作Service实现
* @createDate 2022-07-04 13:44:25
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




