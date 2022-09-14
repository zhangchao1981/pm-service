package com.iscas.pm.auth.mapper;

import com.iscas.pm.auth.model.UserBriefInfo;
import com.iscas.pm.common.core.model.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 李昶
 * @Date: 2022-07-06 11:17:11
 * @Description: 针对表【user(用户表)】的数据库操作Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    @Select("select * " +
            "from auth_user " +
            "where user_name= #{userName}")
    User loadUserByUsername(@Param("userName") String userName);


    @Select("select auth_user.employee_name  as employName,auth_department.name as departmentName,auth_user.id as userId ,auth_user.user_name as userName " +
            "from auth_user " + "left join auth_department  " + "on auth_user.department_id=auth_department.id " +
            "where auth_user.status= #{status} ")
    List<UserBriefInfo> loadUserBriefInfo(@Param("status") String status);

}




