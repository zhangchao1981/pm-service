package com.iscas.pm.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.auth.domain.AuthRolePermission;
import com.sun.org.apache.xpath.internal.operations.Plus;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 66410
* @description 针对表【auth_role_permission】的数据库操作Mapper
* @createDate 2022-07-18 20:53:25
* @Entity auth.domain.AuthRolePermission
*/
@Mapper
public interface AuthRolePermissionMapper extends BaseMapper<AuthRolePermission> {

//    @Select("SELECT t_user.user_name " +
//            " FROM t_blog, t_user " +
//            " WHERE t_blog.id = #{id} " +
//            "     AND t_blog.user_id = t_user.id")
//    String findUserNameByBlogId(@Param("id") Long id);

    //SELECT auth_role_permission.permission_id FROM `auth_role_permission` INNER JOIN auth_user_role ON auth_user_role.role_id=auth_role_permission.role_id where auth_user_role.user_id=1;
    @Select("SELECT auth_role_permission.permission_id " +
            " FROM auth_role_permission " +"INNER JOIN auth_user_role "
            +"ON auth_user_role.role_id=auth_role_permission.role_id "+
            " WHERE auth_user_role.user_id=#{userid}" )
    List<String> getPerMissionByUserId(@Param("userid") Integer userid);
}




