package com.iscas.pm.auth.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.domain.User;
import com.iscas.pm.auth.domain.UserLoginParam;
import com.iscas.pm.auth.mapper.UserMapper;
import com.iscas.pm.auth.service.UserService;
import com.iscas.pm.common.core.util.RSACoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author 66410
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @createDate 2022-07-06 11:17:11
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {
    //        user.setPassword(RSACoder.encryptByPublicKey(user.getPassword()));
//        userMapper.insert(user);

    @Resource
    private UserMapper userMapper;

    @Override
    public User get(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public User addUser(User user) {
        //根据姓名生成用户名

        //设置初始密码

        //存储人员信息
        userMapper.insert(user);
        return user;
    }

    @Override
    public Boolean changePassword(String username, String oldPwd, String newPwd) {
        //数据库中查询出用户信息
        User user = userMapper.loadUserByUsername(username);

        if (!oldPwd.equals(RSACoder.decryptByPrivateKey(user.getPassword()))) {
            throw new IllegalArgumentException("旧密码填写错误");
        }

        if (Objects.equals(oldPwd, newPwd)) {
            throw new IllegalArgumentException("请不要改成旧密码");
        }

        //加密密码后更新数据库
        user.setPassword(RSACoder.encryptByPublicKey(newPwd));
        userMapper.updateById(user);
        return true;
    }

    @Override
    public User loadUserByUsername(String username) {
        return userMapper.loadUserByUsername(username);
    }

}




