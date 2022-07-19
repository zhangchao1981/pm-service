package com.iscas.pm.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.domain.AuthRolePermission;
import com.iscas.pm.auth.mapper.AuthRolePermissionMapper;
import com.iscas.pm.auth.mapper.AuthUserRoleMapper;
import com.iscas.pm.auth.service.AuthRolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 66410
* @description 针对表【auth_role_permission】的数据库操作Service实现
* @createDate 2022-07-18 20:53:25
*/
@Service
public class AuthRolePermissionServiceImpl extends ServiceImpl<AuthRolePermissionMapper, AuthRolePermission>
    implements AuthRolePermissionService {
@Autowired
AuthRolePermissionMapper authRolePermissionMapper;
@Autowired
AuthUserRoleMapper authUserRoleMapper;
    @Override
    public List<String> getPermissionsByUserId(Integer userId) {

//        QueryWrapper<AuthRolePermission> queryWrapper = new QueryWrapper<>();
//        queryWrapper.eq("auth_user_role.user_id",userId);
        return authRolePermissionMapper.getPerMissionByUserId(userId);
    }
}




