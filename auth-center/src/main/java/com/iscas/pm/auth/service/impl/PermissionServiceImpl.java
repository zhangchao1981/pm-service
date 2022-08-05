package com.iscas.pm.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.model.Permission;
import com.iscas.pm.auth.model.ProjectPermission;
import com.iscas.pm.auth.service.PermissionService;
import com.iscas.pm.auth.mapper.PermissionMapper;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;

/**
 * @author 66410
 * @description 针对表【auth_permission】的数据库操作Service实现
 * @createDate 2022-07-18 18:47:01
 */
@Service
public class PermissionServiceImpl extends ServiceImpl<PermissionMapper, Permission> implements PermissionService {
    @Resource
    PermissionMapper permissionMapper;

    @Override
    public List<String> getSystemPerMissions(Integer userid) {
        return permissionMapper.getSystemPerMissions(userid);
    }

    @Override
    public List<ProjectPermission> getProjectPermissions(Integer userId) {
        return permissionMapper.getProjectPermissions(userId);
    }
}




