package com.iscas.pm.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.project.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 李昶
* @description 针对表【pm_role_permission】的数据库操作Mapper
* @createDate 2022-07-20 15:39:15
* @Entity api.model/project.RolePermission
*/
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    @Select("SELECT permission_id " +
            " FROM pm_role_permission " +
            " WHERE  role_id=#{roleId}")
    List<String> selectList(Integer roleId);

//    已经换成从thread里面取了
//    @Select("SELECT pm_role_permission.permission_id " +
//            " FROM pm_role_permission" +"INNER JOIN pm_project_user_role "
//            +"ON pm_project_user_role.role_id=pm_role_permission.role_id "+
//            " WHERE pm_project_user_role.user_id=#{userid}"+"AND pm_project_user_role.project_id=#{projectid}" )
//    List<String> getPerMissions(@Param("userid") Integer userid,@Param("projectid") Integer projectid);

}




