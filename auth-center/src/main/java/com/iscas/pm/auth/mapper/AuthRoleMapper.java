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

    @Select("SELECT *" + " FROM auth_role " +"INNER JOIN auth_user_role "
            +"ON  auth_role.id=auth_user_role.role_id  "+
            " WHERE  auth_user_role.user_id="+
            "(select  user_id"+"from"+"where auth_user.user_name="+"#{userName}"     )
    List<AuthRole> getRolesByUserName(String userName);
}




