package com.iscas.pm.auth.controller;


import com.iscas.pm.auth.domain.ModifyPwdParam;
import com.iscas.pm.auth.domain.User;
import com.iscas.pm.auth.domain.UserLogin;
import com.iscas.pm.auth.mapper.UserMapper;
import com.iscas.pm.auth.service.UserService;
import com.iscas.pm.common.core.util.Wrapper;
import com.iscas.pm.common.core.web.exception.AuthenticateException;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 李昶
 */
@Wrapper
@Slf4j
@RestController
@RequestMapping("/usercore")
@Api(tags = {"用户管理"})
public class UserController {
    @Autowired
    UserService userService;
    @Resource
    private UserMapper userMapper;
//    @Autowired
//    private RedisUtil redisUtil;

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


    /***
     * 新增User数据
     * @param   userlogin
     * @return
     */
    @ApiOperation(value = "用户新增")
    @PostMapping("adduser")
    @ApiImplicitParam(name = "userlogin",value = "username,password", required = true, dataType = "UserLogin", paramType = "body")
    public User adduser(@RequestBody @Valid UserLogin userlogin) {
        if (userMapper.existsByName(userlogin.getUsername())) {
            throw new RuntimeException("该用户名已被使用，请重新输入");
        }
        User user = userService.addUser(userlogin);
        user.setPassword(null);
        return user;
    }

    /***
     * 修改User密码
     * @param   param  里面封装了用户请求的 用户名，旧密码，新密码
     * @return
     */
    @ApiOperation(value = "修改用户密码")
    @PostMapping(value = "/change")
    @ApiImplicitParam(name = "param",value = "userName,oldPassWord,newPassWord", required = true, dataType = "ModifyPwdParam", paramType = "body")
    public Boolean change(@RequestBody ModifyPwdParam param) {
      return userService.change(param.getUserName(), param.getOldPassWord(), param.getNewPassWord());
    }



}
