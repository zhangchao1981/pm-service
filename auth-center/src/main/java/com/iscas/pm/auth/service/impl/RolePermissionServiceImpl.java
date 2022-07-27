package com.iscas.pm.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.domain.RolePermission;
import com.iscas.pm.auth.mapper.RolePermissionMapper;
import com.iscas.pm.auth.mapper.AuthUserRoleMapper;
import com.iscas.pm.auth.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
 * @author 66410
 * @description 针对表【auth_role_permission】的数据库操作Service实现
 * @createDate 2022-07-18 20:53:25
 */
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission> implements RolePermissionService {

}




