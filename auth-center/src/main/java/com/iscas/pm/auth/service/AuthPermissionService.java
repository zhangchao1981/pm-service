package com.iscas.pm.auth.service;

import com.iscas.pm.auth.domain.AuthPermission;
import com.baomidou.mybatisplus.extension.service.IService;

/**
* @author 66410
* @description 针对表【auth_permission】的数据库操作Service
* @createDate 2022-07-18 18:47:01
*/
public interface AuthPermissionService extends IService<AuthPermission> {

    String findPermissionNameById(String permissionId);

}
