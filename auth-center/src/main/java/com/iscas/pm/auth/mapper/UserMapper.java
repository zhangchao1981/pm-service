package com.iscas.pm.auth.mapper;

import com.iscas.pm.auth.domain.user.User;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * @author 李昶
 * @Date: 2022-07-06 11:17:11
 * @Description: 针对表【user(用户表)】的数据库操作Mapper
 */
@Mapper
public interface UserMapper extends BaseMapper<User> {

    User loadUserByUsername(String userName);

}




