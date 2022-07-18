package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.model.project.Project;
import com.iscas.pm.api.mapper.ProjectMapper;
import com.iscas.pm.api.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
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

    @Override
    public List<Project> get(){
        return projectMapper.selectList(null);
    }
}




