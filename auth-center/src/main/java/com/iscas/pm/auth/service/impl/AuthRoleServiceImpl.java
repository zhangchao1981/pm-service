package com.iscas.pm.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.domain.AuthRole;
import com.iscas.pm.auth.mapper.AuthRoleMapper;
import com.iscas.pm.auth.service.AuthRoleService;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author 66410
* @description 针对表【auth_role】的数据库操作Service实现
* @createDate 2022-07-18 20:45:28
*/
@Service
public class AuthRoleServiceImpl extends ServiceImpl<AuthRoleMapper, AuthRole>
    implements AuthRoleService {
    @Autowired
    AuthRoleMapper authRoleMapper;



    @Override
    public List<AuthRole> selectRoleList(String name, String type) {
        QueryWrapper<AuthRole> queryWrapper = new QueryWrapper<>();
        queryWrapper.like(!StringUtils.isEmpty(name),"name",name);
        queryWrapper.eq(!StringUtils.isEmpty(type),"type",type);
        return authRoleMapper.selectList(queryWrapper);
    }



    @Override
    public List<AuthRole> getRolesByUserName(String userName, String type) {
        return  authRoleMapper.getRolesByUserName(userName);
    }
}




