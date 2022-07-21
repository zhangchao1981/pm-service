package com.iscas.pm.auth.service;

import com.iscas.pm.auth.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.common.core.model.UserDetailInfo;

/**
* @author 66410
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2022-07-06 11:17:11
*/
public interface UserService  extends IService<User>{

    User get(Integer userId);

    User addUser(User user);

    Boolean changePassword(String username, String oldPwd, String newPwd);

    User loadUserByUsername(String username);

    UserDetailInfo getUserDetails(String userName, String projectId);
}
