package com.iscas.pm.api.mapper;


import com.iscas.pm.api.model.project.Project;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 66410
* @description 针对表【pjdata】的数据库操作Mapper
* @createDate 2022-07-04 13:44:25
* @Entity com.doc.domain.Pjdata
*/
@Mapper
public interface ProjectMapper{
    List<Project> selectAllById(@Param("id") String id);

    void addProject(Project project);

}




