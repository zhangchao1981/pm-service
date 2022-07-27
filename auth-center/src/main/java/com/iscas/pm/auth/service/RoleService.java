package com.iscas.pm.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.auth.domain.Permission;
import com.iscas.pm.auth.domain.Role;

import java.util.List;

/**
 * @author lichang
 * @description 针对表【auth_role】的数据库操作Service
 * @createDate 2022-07-18 20:45:28
 */
public interface RoleService extends IService<Role> {

    List<Role> selectRoleList(String name, String type);

    Boolean addRolePermissions(Integer roleId, List<String> permissionIds);

    List<Permission> getRolePermissions(Integer roleId);

    Boolean existRole(Integer roleId, String roleName);

}
