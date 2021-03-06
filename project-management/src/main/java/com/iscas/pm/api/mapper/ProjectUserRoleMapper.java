package com.iscas.pm.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.project.ProjectUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 李昶
* @description 针对表【pm_project_user_role】的数据库操作Mapper
* @createDate 2022-07-20 15:40:12
* @Entity api.model/project.ProjectUserRole
*/
@Mapper
public interface ProjectUserRoleMapper extends BaseMapper<ProjectUserRole> {

}




