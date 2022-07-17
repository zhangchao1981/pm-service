package com.iscas.pm.auth.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.domain.User;
import com.iscas.pm.auth.domain.UserLogin;
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
        User user = new User().setUsername(userlogin.getUsername()).setPassword(RSACoder.encryptByPublicKey(userlogin.getPassword()));
        userMapper.insert(user);
        return user;
    }

    @Override
    public Boolean change(String username, String oldPwd,String newPwd) {
        User user = userMapper.loadUserByUsername(username);
        //如果用户密码正确，则可以更改密码

//        boolean tag = BCrypt.checkpw(oldPwd, user.getPassword());
        //查询数据库里存的用户旧密码，验证是否和用户输入的旧密码相同 (用RSACode 私钥解密)
        boolean tag =oldPwd.equals(RSACoder.decryptByPrivateKey(user.getPassword()));
        if (!tag) {
            throw new IllegalArgumentException("旧密码填写错误");
        }

        if (Objects.equals(oldPwd, newPwd)) {
            throw new IllegalArgumentException("请不要改成旧密码");
        }
        if (tag){
            user.setPassword( RSACoder.encryptByPublicKey(newPwd));
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




