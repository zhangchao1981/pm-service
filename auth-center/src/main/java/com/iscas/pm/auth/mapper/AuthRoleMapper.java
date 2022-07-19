package com.iscas.pm.auth.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.auth.domain.AuthRole;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 66410
* @description 针对表【auth_role】的数据库操作Mapper
* @createDate 2022-07-18 20:45:28
* @Entity auth.domain.AuthRole
*/
@Mapper
public interface AuthRoleMapper extends BaseMapper<AuthRole> {



}




