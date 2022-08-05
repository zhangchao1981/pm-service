package com.iscas.pm.auth.mapper;

import com.iscas.pm.auth.model.Permission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.auth.model.ProjectPermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author lichang
 * @description 针对表【auth_permission】的数据库操作Mapper
 * @createDate 2022-07-18 18:47:01
 */
@Mapper
public interface PermissionMapper extends BaseMapper<Permission> {
    @Select("SELECT permission_id " +
            "FROM auth_role_permission INNER JOIN auth_user_role "+
            "ON auth_user_role.role_id=auth_role_permission.role_id " +
            "WHERE auth_user_role.user_id=#{userid}")
    List<String> getSystemPerMissions(@Param("userid") Integer userid);

    @Select("SELECT project_id,permission_id " +
            "FROM pm_project_user_role,auth_role_permission " +
            "WHERE pm_project_user_role.role_id=auth_role_permission.role_id " + " AND user_id =#{userId}")
    List<ProjectPermission> getProjectPermissions(@Param("userId") Integer userId);


}




