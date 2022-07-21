package com.iscas.pm.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.project.RolePermission;

import java.util.List;

/**
* @author 66410
* @description 针对表【pm_role_permission】的数据库操作Service
* @createDate 2022-07-20 15:39:15
*/
public interface RolePermissionService extends IService<RolePermission> {

    List<String> getPermissions(Integer roleId);
}
