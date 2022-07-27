package com.iscas.pm.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.auth.domain.RolePermission;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 66410
* @description 针对表【auth_role_permission】的数据库操作Mapper
* @createDate 2022-07-18 20:53:25
* @Entity auth.domain.AuthRolePermission
*/
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

}




