package com.iscas.pm.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iscas.pm.auth.model.*;
import com.iscas.pm.auth.service.AuthUserRoleService;
import com.iscas.pm.auth.service.DepartmentService;
import com.iscas.pm.common.core.model.User;
import com.iscas.pm.common.core.model.UserStatusEnum;
import com.iscas.pm.auth.service.UserService;
import com.iscas.pm.common.core.model.UserDetailInfo;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiOperationSupport;
import io.swagger.annotations.ApiSort;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.bind.annotation.*;
import springfox.documentation.annotations.ApiIgnore;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;
import java.util.stream.Collectors;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 人员管理
 */
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = {"人员管理"})
@ApiSort(2)
public class UserController {
    @Autowired
    UserService userService;
    @Autowired
    AuthUserRoleService authUserRoleService;
    @Autowired
    DepartmentService departmentService;


    @ApiOperation(value = "人员简要信息列表", notes = "列表返回所有正常状态的人员,支持姓名模糊查询")
    @PostMapping("/userBriefList")
    @PreAuthorize("hasAuthority('/user/userBriefList')")
    public List<UserBriefInfo> userBriefList(){
        return userService.selectUserBriefInfo();
    }

    @ApiOperation(value = "人员列表", notes = "分页查询用户人员列表")
    @PostMapping("/userPageList")
    @PreAuthorize("hasAuthority('/user/userPageList')")
    public IPage<User> userPageList(@RequestBody @Valid UserQueryParam queryParam) {
        return userService.selectUserList(queryParam);
    }




    @ApiOperation(value = "添加人员")
    @PostMapping("addUser")
    @PreAuthorize("hasAuthority('/user/addUser')")
    public User adduser(@Valid @RequestBody User user) {
        userService.addUser(user);
        return user;
    }

    @ApiOperation(value = "编辑人员", notes = "用户id，用户名和姓名都不允许修改")
    @PostMapping("editUser")
    @PreAuthorize("hasAuthority('/user/editUser')")
    public Boolean editUser(@Valid @RequestBody User user) {
        User dbUser = userService.getById(user.getId());
        if (dbUser == null)
            throw new IllegalArgumentException("用户不存在");
        //用户名、姓名、密码都不允许修改
        user.setUserName(dbUser.getUserName());
        user.setEmployeeName(dbUser.getEmployeeName());
        user.setPassword(dbUser.getPassword());

        userService.saveOrUpdate(user);
        return true;
    }

    @ApiOperation(value = "注销人员")
    @PostMapping("cancelUser")
    @PreAuthorize("hasAuthority('/user/cancelUser')")
    public Boolean cancelUser(@NotBlank @RequestParam String userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("注销的用户不存在");
        }
        user.setStatus(UserStatusEnum.CANCEL);
        userService.saveOrUpdate(user);
        //todo 缺少清空token信息，待开发
        return true;
    }

    @ApiOperation(value = "重新启用人员")
    @PostMapping("enableUser")
    @PreAuthorize("hasAuthority('/user/enableUser')")
    public Boolean enableUser(@NotBlank @RequestParam String userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("启用的用户不存在");
        }
        user.setStatus(UserStatusEnum.NORMAL);
        userService.saveOrUpdate(user);

        return true;
    }

    @ApiOperation(value = "分配角色", notes = "为指定用户分配系统角色:(删除用户的角色：传空参)")
    @PostMapping("settingSystemRole")
    @PreAuthorize("hasAuthority('/user/settingSystemRole')")
    public Boolean settingSystemRole(@Valid @RequestBody SettingSystemRoleQueryParam queryParam) {
        if (userService.getById(queryParam.getUserId()) == null) {
            throw new IllegalArgumentException("用户不存在"); }
        return userService.addUserRoles(queryParam.getUserId(),queryParam.getRoles());
    }


    @ApiOperation(value = "修改用户密码")
    @PostMapping(value = "/changePassword")
    public Boolean changePassword(@Valid @RequestBody ModifyPwdParam modifyPwdParam) {
        return userService.changePassword(RequestHolder.getUserInfo().getUserName(),modifyPwdParam.getOldPassword(), modifyPwdParam.getNewPassword());
    }

    @ApiOperation(value = "重置密码")
    @GetMapping("/resetPassWord")
    @PreAuthorize("hasAuthority('/user/resetPassWord')")
    public Boolean resetPassWord(@NotNull @RequestParam("userId") Integer userId) {
        User user = userService.getById(userId);
        if (user == null) {
            throw new IllegalArgumentException("用户不存在");
        }
        user.setPassword(new BCryptPasswordEncoder().encode("123456"));
        userService.saveOrUpdate(user);
        return true;
    }

    @ApiOperation(value = "获取用户信息", notes = "获取当前登录用户信息")
    @GetMapping("/getUserDetails")
    @ApiIgnore
    public UserDetailInfo getUserDetails(@RequestParam String userName) {
        return userService.getUserDetails(userName);
    }


    @ApiOperation(value = "获取用户已分配的系统角色", notes = "根据userId查询对应的系统角色")
    @PostMapping("systemRolesByUserId")
    @PreAuthorize("hasAuthority('/user/systemRolesByUserId')")
    public List<Integer> systemRolesByUserId(@NotNull @RequestParam Integer userId) {
        List<AuthUserRole> roleList = authUserRoleService.list(new QueryWrapper<AuthUserRole>().eq("user_id", userId));
        return roleList.stream().map(AuthUserRole::getRoleId).collect(Collectors.toList());
    }

    @GetMapping("/findDepartment")
    @ApiOperation(value = "查询部门树", notes = "查询整棵部门树")
    @ApiOperationSupport(order = 1)
    @PreAuthorize("hasAuthority('/user/findDepartment')")
    public List<Department> findDepartment() {
        return departmentService.getDepartmentTree();
    }


    @PostMapping("/addDepartment")
    @ApiOperation(value = "添加部门", notes = "id自动生成，前端不用传,children属性是查询显示的，添加不传该值")
    @ApiOperationSupport(order = 2)
    @PreAuthorize("hasAuthority('/user/addDepartment')")
    public Department addDepartment(@Valid @RequestBody Department Department) {
        return departmentService.addDepartment(Department);
    }


    @PostMapping("/deleteDepartment")
    @ApiOperation(value = "删除部门", notes = "根据部门id删除")
    @ApiOperationSupport(order = 3)
    @PreAuthorize("hasAuthority('/user/deleteDepartment')")
    public boolean deleteDepartment(@NotNull(message = "部门Id不能为空") @RequestParam Integer id) {
        //有人员或子部门时，不允许删除
        if (departmentService.list(new QueryWrapper<Department>().eq("parent_id",id)).size()>0){
            throw new IllegalArgumentException("有子部门存在，拒绝删除");
        }
        if (userService.list(new QueryWrapper<User>().eq("department_id",id)).size()>0){
            throw new IllegalArgumentException("该部门有员工存在，拒绝删除");
        }

        if (!departmentService.removeById(id)) {
            throw new IllegalArgumentException("id对应部门不存在");
        }
        return true;
    }

    @PostMapping("/editDepartment")
    @ApiOperation(value = "修改部门", notes = "修改部门名称(或父id)， children属性是查询显示的，修改不传该值")
    @ApiOperationSupport(order = 4)
    @PreAuthorize("hasAuthority('/user/editDepartment')")
    public Department editDepartment(@Valid @RequestBody Department Department) {
        return departmentService.editDepartment(Department);
    }



}
