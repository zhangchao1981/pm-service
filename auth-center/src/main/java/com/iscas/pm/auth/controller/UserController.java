package com.iscas.pm.auth.controller;

import com.iscas.pm.auth.domain.ModifyPwdParam;
import com.iscas.pm.auth.domain.user.User;
import com.iscas.pm.auth.domain.user.UserInfo;
import com.iscas.pm.auth.domain.user.UserStatusEnum;
import com.iscas.pm.auth.service.UserService;
import com.iscas.pm.common.core.model.UserDetailInfo;
import com.iscas.pm.common.core.web.exception.AuthenticateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 人员管理
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = {"人员管理"})
public class UserController {
    @Autowired
    UserService userService;

    @ApiOperation(value = "人员列表",notes = "分页返回人员列表")
    @GetMapping("/userList")
    @PreAuthorize("hasAuthority('/user/userList')")
    public List<User> listAll(@RequestParam String userName, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        //密码全部置空
        return userService.list();
    }

    @ApiOperation(value = "添加人员")
    @PostMapping("addUser")
    @PreAuthorize("hasAuthority('/user/addUser')")
    public User adduser(@RequestBody @Valid User user) {
        userService.addUser(user);

        //password不返回，避免安全隐患
        user.setPassword(null);
        return user;
    }

    @ApiOperation(value = "编辑人员",notes = "用户id，用户名和姓名都不允许修改")
    @PostMapping("editUser")
    @PreAuthorize("hasAuthority('/user/editUser')")
    public User editUser(@RequestBody @Valid User user) {
        userService.saveOrUpdate(user);
        return user;
    }

    @ApiOperation(value = "注销人员")
    @PostMapping("cancelUser")
    @PreAuthorize("hasAuthority('/user/cancelUser')")
    public Boolean cancelUser(@RequestParam String userId) {
        User user = userService.getById(userId);
        if (user==null)
            throw new IllegalArgumentException("注销的用户不存在");

        user.setStatus(UserStatusEnum.CANCEL);
        userService.saveOrUpdate(user);

        return true;
    }

    @ApiOperation(value = "重新启用人员")
    @PostMapping("enableUser")
    @PreAuthorize("hasAuthority('/user/enableUser')")
    public Boolean enableUser(@RequestParam String userId) {
        User user = userService.getById(userId);
        if (user==null)
            throw new IllegalArgumentException("启用的用户不存在");

        user.setStatus(UserStatusEnum.NORMAL);
        userService.saveOrUpdate(user);

        return true;
    }

    @ApiOperation(value = "分配角色",notes = "为指定人员分配系统角色")
    @PostMapping("settingSystemRole")
    @PreAuthorize("hasAuthority('/user/settingSystemRole')")
    public User settingSystemRole(@RequestBody @Valid User user) {
        return user;
    }

    @ApiOperation(value = "修改用户密码")
    @PostMapping(value = "/changePassword")
    public Boolean change(@RequestBody @Valid ModifyPwdParam modifyPwdParam) {
        return userService.changePassword(modifyPwdParam.getUserName(), modifyPwdParam.getOldPassword(), modifyPwdParam.getNewPassword());
    }

    @ApiOperation(value = "重置密码")
    @GetMapping("/resetPassWord")
    @PreAuthorize("hasAuthority('/user/resetPassWord')")
    public String resetPassWord(@RequestParam("userId") Integer userId) {
        return null;
    }

    @ApiOperation(value = "获取用户信息",notes = "获取当前登录用户信息")
    @GetMapping("/getUserDetails")
    public UserDetailInfo getUserDetails(@RequestParam String userName, @RequestParam String projectId) {
        return userService.getUserDetails(userName,projectId);
    }

}
