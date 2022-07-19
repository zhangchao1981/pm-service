package com.iscas.pm.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.domain.PmProjectUserRole;
import com.iscas.pm.auth.service.PmProjectUserRoleService;
import com.iscas.pm.auth.mapper.PmProjectUserRoleMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

/**
* @author 66410
* @description 针对表【pm_project_user_role】的数据库操作Service实现
* @createDate 2022-07-19 13:57:37
*/
@Service
public class PmProjectUserRoleServiceImpl extends ServiceImpl<PmProjectUserRoleMapper, PmProjectUserRole>
    implements PmProjectUserRoleService{

}




