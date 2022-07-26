package com.iscas.pm.auth.service.impl;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.domain.AuthUserRole;
import com.iscas.pm.auth.domain.ProjectPermission;
import com.iscas.pm.auth.domain.user.User;
import com.iscas.pm.auth.domain.user.UserStatusEnum;
import com.iscas.pm.auth.mapper.UserMapper;
import com.iscas.pm.auth.service.AuthRolePermissionService;
import com.iscas.pm.auth.service.PmRolePermissionService;
import com.iscas.pm.auth.service.UserService;
import com.iscas.pm.auth.utils.BCrypt;
import com.iscas.pm.common.core.model.UserDetailInfo;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.annotation.Resource;
import java.util.*;
import java.util.stream.Collectors;

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
    @Autowired
    private PmRolePermissionService pmRolePermissionService;

    @Autowired
    private AuthUserRoleServiceImpl  authUserRoleService;

    @Override
    public User get(Integer id) {
        return userMapper.selectById(id);
    }

    @Override
    public User addUser(User user) {
        //设置初始密码
        user.setPassword(new BCryptPasswordEncoder().encode("123456"));
        //人员姓名转成用户名（姓名全拼，用户名如有重复后面追加01，02 ...）
        //插入用户表
        userMapper.insert(user);
        user.setPassword(null);
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
    public UserDetailInfo getUserDetails(String userName) {
        User user = loadUserByUsername(userName);
        if (user == null) {
            return null;
        }

        //返回系统角色对应的权限列表，去重
        List<String> SystemPermissions = authRolePermissionService.getPermissionsByUserId(user.getId());
        SystemPermissions = SystemPermissions.stream().distinct().collect(Collectors.toList());
        //String permissions_str = StringUtils.join(SystemPermissions, ",");

        //获取所有项目角色对应的权限列表
        List<ProjectPermission> projectPermissionList = pmRolePermissionService.selectProjectPermissions(user.getId());
            Map<String, List<String>> projectPermissions = projectPermissionList.stream().collect(Collectors.groupingBy(ProjectPermission::getProjectId, Collectors.mapping(ProjectPermission::getPermissionId, Collectors.toList())));

        //封装成UserDetails对象
        UserDetailInfo userDetailInfo = new UserDetailInfo(user.getId(), userName, user.getPassword(), user.getEmployeeName(), user.getStatus() == UserStatusEnum.NORMAL, SystemPermissions,projectPermissions);
        return userDetailInfo;
    }

    @Override
    public IPage<User> selectUserList(String userName, Integer pageNum, Integer pageSize) {
        Page<User> userPage = new Page<>(pageNum,pageSize);
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like(StringUtils.isEmpty(userName),"user_name",userName);
        IPage<User> userIPage = userMapper.selectPage(userPage, wrapper);
        //需要把返回的字段改一下
        List<User> records = userIPage.getRecords();
        for (int i = 0; i < records.size(); i++) {
            records.get(i).setPassword(null);
        }
        return userIPage;
    }

    @Override
    public Boolean adduserroles(Integer userid, List<Integer> roles) {

        AuthUserRole authUserRole = new AuthUserRole();
        authUserRole.setUserId(userid);
        //先把原来分配的角色删了
        QueryWrapper<AuthUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id",userid);
        authUserRoleService.remove(wrapper);

        for (int i = 0; i < roles.size(); i++) {
            authUserRole.setRoleId(roles.get(i));
            if (!authUserRoleService.save(authUserRole)){
                return false;
            }
        }
        return null;
    }
}
