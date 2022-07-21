package com.iscas.pm.auth.service.impl;


import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.domain.User;
import com.iscas.pm.auth.domain.UserStatusEnum;
import com.iscas.pm.auth.mapper.UserMapper;
import com.iscas.pm.auth.service.AuthRolePermissionService;
import com.iscas.pm.auth.service.UserService;
import com.iscas.pm.auth.utils.BCrypt;
import com.iscas.pm.common.core.model.UserDetailInfo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.List;
import java.util.Objects;

/**
 * @author lichang
 * @description 针对表【user(用户表)】的数据库操作Service实现
 * @Date: 2019/7/7 16:23
 */
@Service
public class UserServiceImpl extends ServiceImpl<UserMapper, User> implements UserService {

    @Resource
    private UserMapper userMapper;
    @Autowired
    private AuthRolePermissionService authRolePermissionService;

    @Override
    public User get(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public User addUser(User user) {
        userMapper.insert(user);
        return user;
    }

    @Override
    public Boolean changePassword(String username, String oldPwd, String newPwd) {
        User user = userMapper.loadUserByUsername(username);
        //如果用户密码正确，则可以更改密码

        //查询数据库里存的用户旧密码，验证是否和用户输入的旧密码相同 (密码加密方式： BCryptPasswordEncoder()  )
        boolean tag = BCrypt.checkpw(oldPwd, user.getPassword());

        if (!tag) {
            throw new IllegalArgumentException("旧密码填写错误");
        }

        if (Objects.equals(oldPwd, newPwd)) {
            throw new IllegalArgumentException("请不要改成旧密码");
        }
        if (tag) {
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

    @Override
    public UserDetailInfo getUserDetails(String userName, String projectId) {
        User user = loadUserByUsername(userName);
        if (user == null) {//有可能没有数据
            return null;
        }
        //这里加载的pwd是用于验证的，也就是数据库存的用户密码，如果用户登录login输入的密码不是这个，就会报错
        String pwd = user.getPassword();//从数据库中查到的密码
        //获取系统角色下对应的权限列表 user_role
        List<String> permissionsList = authRolePermissionService.getPermissionsByUserId(user.getId());
        String permissions = "";//这里应该写成从数据库里获取，但是由于我们的表中没存，所以就简化了

        for (String permission : permissionsList) {
            permissions += permission + ",";
        }
        permissions = permissions.substring(0, permissions.length() - 1);

        UserDetailInfo userDetailInfo = new UserDetailInfo(user.getId(),userName,user.getPassword(),user.getEmployeeName(),user.getStatus()== UserStatusEnum.NORMAL,permissions);
        return userDetailInfo;
    }
}
