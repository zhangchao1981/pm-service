package com.iscas.pm.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.auth.domain.Permission;
import com.iscas.pm.auth.domain.Role;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 66410
 * @description 针对表【auth_role】的数据库操作Mapper
 * @createDate 2022-07-18 20:45:28
 * @Entity auth.domain.AuthRole
 */
@Mapper
public interface RoleMapper extends BaseMapper<Role> {
    @Select("SELECT * " +
            "FROM auth_role_permission,auth_permission " +
            "WHERE auth_role_permission.permission_id = auth_permission.id AND role_id = #{roleId}")
    List<Permission> getRolePermissions(@Param("roleId") Integer roleId);
}




