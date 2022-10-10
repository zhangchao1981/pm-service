package com.iscas.pm.auth.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.auth.controller.AuthController;
import com.iscas.pm.auth.mapper.DepartmentMapper;
import com.iscas.pm.auth.model.AuthUserRole;
import com.iscas.pm.auth.model.ProjectPermission;
import com.iscas.pm.auth.model.UserBriefInfo;
import com.iscas.pm.auth.model.UserQueryParam;
import com.iscas.pm.common.core.model.User;
import com.iscas.pm.common.core.model.UserStatusEnum;
import com.iscas.pm.auth.mapper.UserMapper;
import com.iscas.pm.auth.service.PermissionService;
import com.iscas.pm.auth.service.UserService;
import com.iscas.pm.auth.utils.BCryptUtil;
import com.iscas.pm.common.core.model.UserDetailInfo;
import com.iscas.pm.common.core.util.Pinyin4jUtil;
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
    private PermissionService permissionService;
    @Autowired
    private AuthUserRoleServiceImpl authUserRoleService;
    @Autowired
    private DepartmentMapper departmentMapper;

    @Override
    public User addUser(User user) {
        //设置初始密码
        user.setPassword(new BCryptPasswordEncoder().encode("123456"));

        //人员姓名全拼生成用户名
        String userName = Pinyin4jUtil.getPingYin(user.getEmployeeName());

        //重名后面追加0x
        String duplicateUserName = userName;
        int i = 1;
        while (loadUserByUsername(duplicateUserName) != null) {
            duplicateUserName = userName + "0" + i;
            i++;
        }
        user.setUserName(duplicateUserName);

        //补全信息后，插入用户表
        user.setCreateTime(new Date());
        user.setUpdateTime(new Date());
        user.setStatus(UserStatusEnum.NORMAL);
        userMapper.insert(user);

        //密码置空
        user.setPassword(null);
        return user;
    }

    @Override
    public Boolean changePassword(String userName, String oldPwd, String newPwd) {
        User user = userMapper.loadUserByUsername(userName);
        if (user == null)
            throw new IllegalArgumentException("用户不存在");

        if (!BCryptUtil.checkpw(oldPwd, user.getPassword())) {
            throw new IllegalArgumentException("旧密码填写错误");
        }

        if (Objects.equals(oldPwd, newPwd)) {
            throw new IllegalArgumentException("请不要改成旧密码");
        }

        if (BCryptUtil.checkpw(newPwd, AuthController.getDefaultPassword())) {
            throw new IllegalArgumentException("不允许修改为初始密码");
        }

        //更新密码
        user.setPassword(new BCryptPasswordEncoder().encode(newPwd));
        userMapper.updateById(user);
        return true;
    }

    @Override
    public User loadUserByUsername(String userName) {
        return userMapper.loadUserByUsername(userName);
    }

    @Override
    public UserDetailInfo getUserDetails(String userName) {
        User user = loadUserByUsername(userName);
        if (user == null) {
            return null;
        }

        //返回系统角色对应的权限列表，去重
        List<String> systemPermissions = permissionService.getSystemPerMissions(user.getId());
        systemPermissions = systemPermissions.stream().distinct().collect(Collectors.toList());

        //获取所有项目角色对应的权限列表
        List<ProjectPermission> projectPermissionList = permissionService.getProjectPermissions(user.getId());
        Map<String, List<String>> projectPermissions = projectPermissionList.stream().collect(Collectors.groupingBy(ProjectPermission::getProjectId, Collectors.mapping(ProjectPermission::getPermissionId, Collectors.toList())));

        //封装成UserDetails对象
        UserDetailInfo userDetailInfo = new UserDetailInfo(user.getId(), userName, user.getPassword(), user.getEmployeeName(), user.getStatus() == UserStatusEnum.NORMAL, systemPermissions, projectPermissions);
        return userDetailInfo;
    }

    @Override
    public IPage<User> selectUserList(UserQueryParam queryParam) {
        //查询条件
        String departmentId = queryParam.getDepartmentId();
        String name = queryParam.getName();
        String status = queryParam.getStatus();

        //封装查询语句
        QueryWrapper<User> wrapper = new QueryWrapper<>();
        wrapper.like(!StringUtils.isEmpty(name), "employee_name", name)
                .or().like(!StringUtils.isEmpty(name), "user_name", name);
        wrapper.eq(!StringUtils.isEmpty(departmentId), "department_id", departmentId);
        wrapper.eq(!StringUtils.isEmpty(status), "status", status);

        Page<User> page = new Page<>(queryParam.getPageNum(), queryParam.getPageSize());
        IPage<User> userPage = userMapper.selectPage(page, wrapper);

        //密码置为空
        List<User> records = userPage.getRecords();
        for (int i = 0; i < records.size(); i++) {
            records.get(i).setPassword(null);
        }
        return userPage;
    }

    @Override
    public List<UserBriefInfo> selectUserBriefInfo() {

        String status = "NORMAL";
        return userMapper.loadUserBriefInfo(status);

    }

    @Override
    public Boolean addUserRoles(Integer userId, List<Integer> roles) {
        //删除原来分配的角色
        QueryWrapper<AuthUserRole> wrapper = new QueryWrapper<>();
        wrapper.eq("user_id", userId);
        authUserRoleService.remove(wrapper);
        if (roles == null || roles.size() < 1) {
            return true;
        }
        //批量插入
        List<AuthUserRole> userRoles = new ArrayList<>();
        for (int i = 0; i < roles.size(); i++) {
            AuthUserRole authUserRole = new AuthUserRole();
            authUserRole.setUserId(userId);
            authUserRole.setRoleId(roles.get(i));
            userRoles.add(authUserRole);
        }
        return authUserRoleService.saveBatch(userRoles);
    }
}
