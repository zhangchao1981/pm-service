package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.RolePermissionMapper;
import com.iscas.pm.api.model.project.RolePermission;
import com.iscas.pm.api.service.RolePermissionService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 66410
* @description 针对表【pm_role_permission】的数据库操作Service实现
* @createDate 2022-07-20 15:39:15
*/
@Service
public class RolePermissionServiceImpl extends ServiceImpl<RolePermissionMapper, RolePermission>
    implements RolePermissionService {
@Autowired
RolePermissionMapper rolePermissionMapper;

    /**
     * 根据roleId查询相应的权限列表
     * @param roleId
     * @return
     */
    @Override
    public List<String> getPermissions(Integer roleId) {
            return   rolePermissionMapper.selectList(roleId);
    }
}




