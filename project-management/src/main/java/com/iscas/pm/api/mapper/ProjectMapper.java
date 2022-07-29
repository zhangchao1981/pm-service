package com.iscas.pm.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iscas.pm.api.model.project.Project;
import com.iscas.pm.api.model.project.ProjectQueryParam;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 项目基本信息dao层接口
 */
@Mapper
public interface ProjectMapper extends BaseMapper<Project> {

    Page<Project> getProjectList(Page<Project> page, @Param("param") ProjectQueryParam param);

    @Select("SELECT name FROM pm_project WHERE id=#{id} or name=#{name}")
    Project findProjectByIdAndName(String id, String name);

    @Select("SELECT name FROM pm_project WHERE id!=#{id} and name=#{name}")
    Project findProjectByNotIdAndName(String id, String name);
}
