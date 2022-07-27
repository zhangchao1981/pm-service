package com.iscas.pm.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iscas.pm.auth.domain.AuthRole;
import com.iscas.pm.auth.domain.user.ListQueryCondition;
import com.iscas.pm.auth.domain.user.User;
import com.iscas.pm.auth.service.AuthRoleService;
import com.iscas.pm.auth.service.UserService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @author by  lichang
 * @date 2022/7/27.
 */
@Slf4j
@RestController
@RequestMapping("/role")
@Api(tags = {"角色管理"})
public class RoleController {
   @Autowired
   AuthRoleService  authRoleService;

    //查询角色列表
    @ApiOperation(value = "角色列表")
    @GetMapping("/roleList")
    public List<AuthRole> listAll() {
        return  authRoleService.list();
    }

    //查询user对应角色信息
    @ApiOperation(value = "人员列表",notes = "分页返回人员列表")
    @GetMapping("/userList")
    public List< AuthRole> listAll(@RequestParam Integer userId) {

        return  authRoleService.list();

    }




    }
