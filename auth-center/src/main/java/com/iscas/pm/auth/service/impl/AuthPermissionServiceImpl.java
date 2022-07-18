package com.iscas.pm.auth.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.domain.AuthPermission;
import com.iscas.pm.auth.service.AuthPermissionService;
import com.iscas.pm.auth.mapper.AuthPermissionMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;

/**
* @author 66410
* @description 针对表【auth_permission】的数据库操作Service实现
* @createDate 2022-07-18 18:47:01
*/
@Service
public class AuthPermissionServiceImpl extends ServiceImpl<AuthPermissionMapper, AuthPermission>
    implements AuthPermissionService{
@Resource
AuthPermissionMapper  authPermissionMapper;

    /**
     * 根据Id查询对应permission name属性
     * @param permissionId
     * @return
     */
    @Override
    public String findPermissionNameById(String permissionId) {
        return authPermissionMapper.selectById(permissionId).getName();
    }
}




