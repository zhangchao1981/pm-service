package com.iscas.pm.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iscas.pm.auth.domain.ModifyPwdParam;
import com.iscas.pm.auth.domain.user.ListQueryCondition;
import com.iscas.pm.auth.domain.user.User;
import com.iscas.pm.auth.domain.user.UserStatusEnum;
import com.iscas.pm.auth.service.UserService;
import com.iscas.pm.common.core.model.UserDetailInfo;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
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
//    @PreAuthorize("hasAuthority('/user/userList')")
    public IPage<User>  listAll( @RequestBody ListQueryCondition condition ) {
        //用户名模糊查询
        //状态  雇员姓名  人员所在部门id
        //密码全部置空
        return userService.selectUserList(condition);
    }

    @ApiOperation(value = "添加人员")
    @PostMapping("addUser")
//    @PreAuthorize("hasAuthority('/user/addUser')")
    public User adduser(@Valid @RequestBody  User user) {
        //初始密码统一设置成123456
        userService.addUser(user);
        return user;
    }

    @ApiOperation(value = "编辑人员",notes = "用户id，用户名和姓名都不允许修改")
    @PostMapping("editUser")
//    @PreAuthorize("hasAuthority('/user/editUser')")
    public User editUser(@Valid @RequestBody  User user) {
        //待新增功能：用户id，用户名和姓名都不允许修改
        User aimUser = userService.get(user.getId());
        user.setUserName(aimUser.getUserName());
        user.setEmployeeName(aimUser.getEmployeeName());

        userService.saveOrUpdate(user);
        return user;
    }

    @ApiOperation(value = "注销人员")
    @PostMapping("cancelUser")
    @PreAuthorize("hasAuthority('/user/cancelUser')")
    public Boolean cancelUser(@NotBlank @RequestParam String userId) {
        User user = userService.getById(userId);
        if (user==null) {
            throw new IllegalArgumentException("注销的用户不存在");
        }
        user.setStatus(UserStatusEnum.CANCEL);
        userService.saveOrUpdate(user);


        //缺少清空token信息，待开发

        return true;
    }

    @ApiOperation(value = "重新启用人员")
    @PostMapping("enableUser")
    @PreAuthorize("hasAuthority('/user/enableUser')")
    public Boolean enableUser(@NotBlank  @RequestParam String userId) {
        User user = userService.getById(userId);
        if (user==null) {
            throw new IllegalArgumentException("启用的用户不存在");
        }
        user.setStatus(UserStatusEnum.NORMAL);
        userService.saveOrUpdate(user);

        return true;
    }


    @ApiOperation(value = "分配角色",notes = "为指定人员分配系统角色")
    @PostMapping("settingSystemRole")
//    @PreAuthorize("hasAuthority('/user/settingSystemRole')")
    public Boolean settingSystemRole(@NotNull  @RequestParam Integer userId, @RequestBody List<Integer> roles) {//多角色分配  user
        //  首先判断user是否在auth_user表中
        if (userService.get(userId)==null){
            throw new IllegalArgumentException("指定人员未注册");
        }
        //  更新/添加  auth_user_role表记录
        return    userService.addUserRoles(userId,roles);

    }

    @ApiOperation(value = "修改用户密码")
    @PostMapping(value = "/changePassword")
    public Boolean change(@Valid  @RequestBody ModifyPwdParam modifyPwdParam) {
        return userService.changePassword(modifyPwdParam.getUserName(), modifyPwdParam.getOldPassword(), modifyPwdParam.getNewPassword());
    }

    @ApiOperation(value = "重置密码")
    @GetMapping("/resetPassWord")
    @PreAuthorize("hasAuthority('/user/resetPassWord')")
    public String resetPassWord(@NotNull @RequestParam("userId") Integer userId) {
        String resetpassword= "$2a$10$BDWORFgZFrZgBgJJptbSC.StH6RkQQA/ZlAQ0i/WEi9wJvHRirZBW";
        User user = userService.get(userId).setPassword(resetpassword);
        userService.saveOrUpdate(user);
        return resetpassword;
    }

    @ApiOperation(value = "获取用户信息",notes = "获取当前登录用户信息")
    @GetMapping("/getUserDetails")
    public UserDetailInfo getUserDetails(@RequestParam String userName) {
        return userService.getUserDetails(userName);
    }

}
