package com.iscas.pm.api.mapper.project;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.project.ProjectUserRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 李昶
* @description 针对表【pm_project_user_role】的数据库操作Mapper
* @createDate 2022-07-20 15:40:12
* @Entity api.model/project.ProjectUserRole
*/
@Mapper
public interface ProjectUserRoleMapper extends BaseMapper<ProjectUserRole> {
    @Select("SELECT pm_project_user_role.id as id,pm_project_user_role.user_id as user_id ,project_id, role_id,employee_name" +
            " FROM pm_project_user_role , auth_user" +
            " WHERE pm_project_user_role.user_id=auth_user.id and project_id=#{projectId}")
    List<ProjectUserRole> selectAllByProjectId(String projectId);

    //SELECT auth_role.`name` as role_name  FROM `auth_role` where auth_role.id in
    //(SELECT distinct pm_project_user_role.role_id FROM  pm_project_user_role where pm_project_user_role.project_id= "project_demo" )


    @Select("SELECT auth_role.`name` as role_name " +" from auth_role  "+" where auth_role.id " +"in("+"select distinct pm_project_user_role.role_id"+"FROM  pm_project_user_role"+"where pm_project_user_role.project_id= #{projectId})")
    List<String> selectAllRoleByProjectId(String projectId);
}




