package com.iscas.pm.auth.service;

import com.iscas.pm.auth.domain.Permission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.auth.domain.ProjectPermission;

import java.util.List;

/**
* @author 66410
* @description 针对表【auth_permission】的数据库操作Service
* @createDate 2022-07-18 18:47:01
*/
public interface PermissionService extends IService<Permission> {

    List<String> getSystemPerMissions(Integer userid);

    List<ProjectPermission> getProjectPermissions(Integer userId);

}
