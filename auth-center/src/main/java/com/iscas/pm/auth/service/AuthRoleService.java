package com.iscas.pm.auth.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.auth.domain.AuthRole;

import java.util.List;

/**
* @author 66410
* @description 针对表【auth_role】的数据库操作Service
* @createDate 2022-07-18 20:45:28
*/
public interface AuthRoleService extends IService<AuthRole> {

    List<AuthRole> getRolesByUserName(String userName, String type);

    List<AuthRole> selectRoleList(String name, String type);

//    List<String> getPermissionsByRoleId(Integer roleid);
}
