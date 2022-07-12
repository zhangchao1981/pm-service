package com.iscas.pm.auth.service;

import com.iscas.pm.auth.domain.UserLogin;
import com.iscas.pm.auth.domain.User;
import com.baomidou.mybatisplus.extension.service.IService;

import javax.validation.Valid;

/**
* @author 66410
* @description 针对表【user(用户表)】的数据库操作Service
* @createDate 2022-07-06 11:17:11
*/
public interface UserService  extends IService<User>{

    User get(Integer userId);

    User addUser(@Valid UserLogin userlogin);

    Boolean change(String username, String oldPwd,String newPwd);

    User loadUserByUsername(String username);
}
