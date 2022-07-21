package com.iscas.pm.auth.controller;

import com.iscas.pm.auth.domain.ModifyPwdParam;
import com.iscas.pm.auth.domain.User;
import com.iscas.pm.auth.domain.UserDetailInfo;
import com.iscas.pm.auth.service.PmRolePermissionService;
import com.iscas.pm.auth.service.UserService;
import com.iscas.pm.auth.utils.UserJwt;
import com.iscas.pm.common.core.web.exception.AuthenticateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = {"人员管理"})
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    PmRolePermissionService pmRolePermissionService;


    @ApiOperation(value = "人员列表",notes = "分页返回人员列表")
    @GetMapping("/userList")
    @PreAuthorize("hasAuthority('/user/userList')")
    public Page<User> listAll(@RequestParam String userName, @RequestParam Integer pageNum, @RequestParam Integer pageSize) {

        return null;
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
        return user;
    }

    @ApiOperation(value = "注销人员")
    @PostMapping("cancelUser")
    @PreAuthorize("hasAuthority('/user/cancelUser')")
    public User cancelUser(@RequestParam String userId) {
        return null;
    }

    @ApiOperation(value = "重新启用人员")
    @PostMapping("enableUser")
    @PreAuthorize("hasAuthority('/user/enableUser')")
    public User enableUser(@RequestParam String userId) {
        return null;
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
    @PostMapping("/resetPassWord")
    @PreAuthorize("hasAuthority('/user/resetPassWord')")
    public String resetPassWord(@RequestParam("userId") Integer userId) {
        return null;
    }

    @ApiOperation(value = "获取用户信息",notes = "获取当前登录用户信息")
    @PostMapping("/getUserDetails")
//    @PreAuthorize("hasAuthority('/user/getUserDetails')")
    public UserDetailInfo getUserDetails(String userId,String projectId) {

        User user = userService.getById(userId);
        if (user == null ) {//有可能没有数据
            return null;
        }
        //这里加载的pwd是用于验证的，也就是数据库存的用户密码，如果用户登录login输入的密码不是这个，就会报错
        String pwd = user.getPassword();//从数据库中查到的密码
        //获取系统角色下对应的权限列表 user_role
        List<String> permissionsList = pmRolePermissionService.getPermissionsByUserIdandProjectId(Integer.parseInt(userId),Integer.parseInt(projectId));
        String permissions = "";//这里应该写成从数据库里获取，但是由于我们的表中没存，所以就简化了

        for (String permission : permissionsList) {
            permissions+=permission+",";
        }
        permissions = permissions.substring(0, permissions.length() - 1);
        UserJwt userDetails = new UserJwt(user.getUserName(), pwd, AuthorityUtils.commaSeparatedStringToAuthorityList(permissions));
        return null;
    }














    //返回用户详情里的密码是加密的
    @ApiOperation(value = "查看用户详情")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Integer", paramType = "path")
    @GetMapping("{userId}")
//    @PreAuthorize("hasPermission(null,'/users/modify')")
    public User finduserbyid(@PathVariable Integer userId) throws AuthenticateException {
        User user = userService.get(userId);
        if (user == null) {
            throw new AuthenticateException("用户未找到");
        }
        return user;
    }

    //返回的用户信息中不带密码(设为null)
    @ApiOperation(value = "根据用户id获取用户信息(不含密码)")
    @ApiImplicitParam(name = "id", required = true, dataType = "Integer", paramType = "query")
    @GetMapping("/getUserById")
    public User getUserById(@RequestParam("id") @NotEmpty Integer id) {
        return userService.get(id).setPassword(null);
    }

    //根据用户名返回完整用户信息
    @ApiOperation(value = "根据用户名获取用户信息(不含密码)")
    @ApiImplicitParam(name = "id", required = true, dataType = "String", paramType = "query")
    @GetMapping("/loadUserByUsername")
    public User loadUserByUsername(@RequestParam("id") @NotEmpty String username) {
        return   userService.loadUserByUsername(username);
    }




}
