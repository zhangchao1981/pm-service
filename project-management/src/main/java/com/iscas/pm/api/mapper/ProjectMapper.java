package com.iscas.pm.api.mapper;

import com.iscas.pm.api.model.project.Project;
import org.apache.ibatis.annotations.Mapper;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 项目基本信息dao层接口
 */
@Mapper
public interface ProjectMapper{
    void addProject(Project project);

}




