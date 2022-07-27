package com.iscas.pm.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.domain.Permission;
import com.iscas.pm.auth.domain.Role;
import com.iscas.pm.auth.domain.RolePermission;
import com.iscas.pm.auth.mapper.RoleMapper;
import com.iscas.pm.auth.service.RolePermissionService;
import com.iscas.pm.auth.service.RoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;

/**
 * @author lichang
 * @description 针对表【auth_role】的数据库操作Service实现
 * @createDate 2022-07-18 20:45:28
 */
@Service
public class RoleServiceImpl extends ServiceImpl<RoleMapper, Role> implements RoleService {
    @Autowired
    RoleMapper roleMapper;
    @Autowired
    RolePermissionService rolePermissionService;

    @Override
    public List<Role> selectRoleList(String name, String type) {
        QueryWrapper<Role> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name), "name", name);
        queryWrapper.eq(!StringUtils.isEmpty(type), "type", type);
        return roleMapper.selectList(queryWrapper);
    }

    @Override
    public Boolean addRolePermissions(Integer roleId, List<String> permissionIds) {
        //删除原来分配的权限
        QueryWrapper<RolePermission> wrapper = new QueryWrapper<>();
        wrapper.eq("role_id", roleId);
        rolePermissionService.remove(wrapper);

        //批量插入
        List<RolePermission> rolePermissions = new ArrayList<>();
        for (int i = 0; i < permissionIds.size(); i++) {
            RolePermission rolePermission = new RolePermission();
            rolePermission.setRoleId(roleId);
            rolePermission.setPermissionId(permissionIds.get(i));
            rolePermissions.add(rolePermission);
        }

        return rolePermissionService.saveBatch(rolePermissions);
    }

    @Override
    public List<Permission> getRolePermissions(Integer roleId) {
        return roleMapper.getRolePermissions(roleId);
    }

    @Override
    public Boolean existRole(Integer roleId, String roleName) {
        QueryWrapper<Role> wrapper = new QueryWrapper<>();
        //新增角色重名校验查询语句
        if (roleId == null) {
            wrapper.eq("name", roleName);
        }else{
            wrapper.eq("name", roleName);
            wrapper.ne("id",roleId);
        }
        List<Role> roles = roleMapper.selectList(wrapper);
        if (roles.size() >0 )
            return true;

        return false;
    }

}




