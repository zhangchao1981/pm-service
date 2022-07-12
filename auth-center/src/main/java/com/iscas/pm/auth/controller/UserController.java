package com.iscas.pm.auth.controller;


import com.auth.exception.NotFoundException;
import com.entity.Result;
import com.entity.StatusCode;
import com.iscas.pm.auth.domain.ModifyPwdParam;
import com.iscas.pm.auth.domain.User;
import com.iscas.pm.auth.domain.UserLogin;
import com.iscas.pm.auth.mapper.UserMapper;
import com.iscas.pm.auth.service.UserService;
import com.iscas.pm.auth.utils.RespBean;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import com.util.RedisUtil;
import com.util.Wrapper;

import javax.annotation.Resource;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;

/**
 * <p>
 *  前端控制器
 * </p>
 *
 * @author 李昶
 * @since 2022-06-05
 */
@Wrapper
@Slf4j
@RestController
@RequestMapping("/user")
@Api(tags = {"用户管理"})
public class UserController {
    @Autowired
    UserService userService;
    @Resource
    private UserMapper userMapper;
    @Autowired
    private RedisUtil redisUtil;

    //返回用户详情里的密码是加密的
    @ApiOperation(value = "查看用户详情")
    @ApiImplicitParam(name = "userId", value = "用户id", required = true, dataType = "Integer", paramType = "path")
    @GetMapping("{userId}")
//    @PreAuthorize("hasPermission(null,'/users/modify')")
    public User finduserbyid(@PathVariable Integer userId) throws NotFoundException {
        User user = userService.get(userId);
        if (user == null) {
            throw new NotFoundException("用户未找到");
        }
        return user;
    }

    //返回的用户信息中不带密码(设为null)
    @ApiOperation(value = "根据用户id获取用户信息(不含密码)")
    @GetMapping("/getUserById")
    public User getUserById(@RequestParam("id") @NotEmpty Integer id) {
        return userService.get(id).setPassword(null);
    }

    //根据用户名返回完整用户信息
    @ApiOperation(value = "根据用户id获取用户信息(不含密码)")
    @GetMapping("/loadUserByUsername")
    public Result<User> loadUserByUsername(@RequestParam("id") @NotEmpty String username) {
        return  new Result<User>(true, StatusCode.OK, "查询成功", userService.loadUserByUsername(username));
    }


    /***
     * 新增User数据
     * @param   userlogin
     * @return
     */
    @ApiOperation(value = "用户新增")
    @PostMapping("adduser")
    public User adduser(@RequestBody @Valid UserLogin userlogin) {
        if (userMapper.existsByName(userlogin.getUsername())) {
            throw new RuntimeException("该用户名已被使用，请重新输入");
        }
        User user = userService.addUser(userlogin);
        user.setPassword(null);
        return user;
//        return userService.addUser(userlogin).setPassword(null);
    }

    /***
     * 修改User密码
     * @param   param  里面封装了用户请求的 用户名，旧密码，新密码
     * @return
     */
    @PostMapping(value = "/change")
    public RespBean change(@RequestBody ModifyPwdParam param) {
        Boolean tag= userService.change(param.getUserName(), param.getOldPassWord(), param.getNewPassWord());
        RespBean respBean = RespBean.build();
        if (tag){
            respBean.setStatus(200);
        }else {
            respBean.setStatus(500);
        }
        return respBean;
    }



}
