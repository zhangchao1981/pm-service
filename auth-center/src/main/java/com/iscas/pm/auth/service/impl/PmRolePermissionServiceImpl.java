package com.iscas.pm.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.domain.PmRolePermission;
import com.iscas.pm.auth.domain.ProjectPermission;
import com.iscas.pm.auth.service.PmRolePermissionService;
import com.iscas.pm.auth.mapper.PmRolePermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 66410
* @description 针对表【pm_role_permission】的数据库操作Service实现
* @createDate 2022-07-19 16:20:19
*/
@Service
public class PmRolePermissionServiceImpl
    implements PmRolePermissionService{
@Autowired
PmRolePermissionMapper pmRolePermissionMapper;



    @Override
    public List<String> getPermissionsByUserIdandProjectId(Integer userId, Integer projectId) {
        return pmRolePermissionMapper.PermissionsByUserIdandProjectId(userId,projectId);
    }

    @Override
    public List<ProjectPermission> selectProjectPermissions(Integer userId) {
        return pmRolePermissionMapper.selectProjectPermissions(userId);
    }
}




