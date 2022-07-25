package com.iscas.pm.auth.mapper;

import com.iscas.pm.auth.domain.PmRolePermission;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 66410
* @description 针对表【pm_role_permission】的数据库操作Mapper
* @createDate 2022-07-19 16:20:19
* @Entity auth.domain.PmRolePermission
*/
@Mapper
public interface PmRolePermissionMapper extends BaseMapper<PmRolePermission> {

    @Select("SELECT pm_role_permission.permission_id " +
            " FROM apm_role_permission " +"INNER JOIN pm_project_user_role "
            +"ON  pm_role_permission.role_id=pm_project_user_role.role_id  "+
            " WHERE  pm_project_user_role.user_id=#{userId} "+"AND pm_project_user_role.project_id=#{projectId}" )
    List<String> PermissionsByUserIdandProjectId(@Param("userId")Integer userId,@Param("projectId") Integer projectId);



}




