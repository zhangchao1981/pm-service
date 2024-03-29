package com.iscas.pm.auth.service;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iscas.pm.auth.model.UserBriefInfo;
import com.iscas.pm.auth.model.UserQueryParam;
import com.iscas.pm.common.core.model.User;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.common.core.model.UserDetailInfo;

import java.util.List;

/**
* @author 66410
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2022-07-06 11:17:11
*/
public interface UserService  extends IService<User>{

    User addUser(User user);

    Boolean changePassword(String userName, String oldPwd, String newPwd);

    User loadUserByUsername(String userName);

    UserDetailInfo getUserDetails(String userName);

    Boolean addUserRoles(Integer userId, List<Integer> roles);

    IPage<User> selectUserList(UserQueryParam condition);

    List<UserBriefInfo> selectUserBriefInfo();
}
