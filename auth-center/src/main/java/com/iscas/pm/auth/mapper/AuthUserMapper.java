package com.iscas.pm.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.auth.domain.AuthUser;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 66410
* @description 针对表【auth_user】的数据库操作Mapper
* @createDate 2022-07-18 20:53:40
* @Entity auth.domain.AuthUser
*/
@Mapper
public interface AuthUserMapper extends BaseMapper<AuthUser> {

}




