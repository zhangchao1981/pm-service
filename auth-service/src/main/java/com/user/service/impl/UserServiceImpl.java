package com.user.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.user.domain.User;
import com.user.domain.UserLogin;
import com.user.mapper.UserMapper;
import com.user.service.UserService;
import com.user.utils.BCrypt;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import com.util.RSACoder;

import javax.annotation.Resource;
import java.util.Objects;

/**
 * @author lichang
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @Date: 2019/7/7 16:23
 */
@Service
public class UserServiceImpl  extends ServiceImpl<UserMapper, User> implements UserService {
    //        user.setPassword(RSACoder.encryptByPublicKey(user.getPassword()));
//        userMapper.insert(user);

    @Resource
    private UserMapper userMapper;

    @Override
    public User get(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public User addUser(UserLogin userlogin) {
        User user = new User().setUsername(userlogin.getUsername()).setPassword( new BCryptPasswordEncoder().encode(userlogin.getPassword()));
        userMapper.insert(user);
        return user;
    }

    @Override
    public Boolean change(String username, String oldPwd,String newPwd) {
        User user = userMapper.loadUserByUsername(username);
        //如果用户密码正确，则可以更改密码

        //查询数据库里存的用户旧密码，验证是否和用户输入的旧密码相同 (密码加密方式： BCryptPasswordEncoder()  )
        boolean tag=  BCrypt.checkpw(oldPwd,user.getPassword());

        if (!tag) {
            throw new IllegalArgumentException("旧密码填写错误");
        }

        if (Objects.equals(oldPwd, newPwd)) {
            throw new IllegalArgumentException("请不要改成旧密码");
        }
        if (tag){
            String encodenewpwd = new BCryptPasswordEncoder().encode(newPwd);
            user.setPassword(encodenewpwd);
        }
        //更新密码
        userMapper.updateById(user);
        return tag;
    }

    @Override
    public User loadUserByUsername(String username) {
        return userMapper.loadUserByUsername(username);
    }

}
