package com.iscas.pm.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.project.RolePermission;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
* @author 66410
* @description 针对表【pm_role_permission】的数据库操作Mapper
* @createDate 2022-07-20 15:39:15
* @Entity api.model/project.RolePermission
*/
@Mapper
public interface RolePermissionMapper extends BaseMapper<RolePermission> {

    @Select("SELECT permission_id " +
            " FROM pm_role_permission " +
            " WHERE  role_id=#{roleId}")
    List<String> selectList(Integer roleId);
}




