package com.iscas.pm.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.auth.domain.AuthUserRole;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 66410
* @description 针对表【auth_user_role】的数据库操作Mapper
* @createDate 2022-07-18 20:53:49
* @Entity auth.domain.AuthUserRole
*/
@Mapper
public interface AuthUserRoleMapper extends BaseMapper<AuthUserRole> {

}




