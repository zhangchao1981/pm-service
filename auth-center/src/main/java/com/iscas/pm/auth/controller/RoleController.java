package com.iscas.pm.auth.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iscas.pm.auth.domain.AuthRole;
import com.iscas.pm.auth.service.AuthRoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import javax.validation.constraints.NotBlank;
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
    AuthRoleService authRoleService;

    //查询角色列表
    @ApiOperation(value = "角色列表",notes = "返回符合条件的角色列表")
    @GetMapping("/roleList")
    public List<AuthRole> listAll(@RequestParam  String name, @RequestParam String type ) {
        return authRoleService.listAll(name,type);
    }

//    @ApiOperation(value = "角色列表", notes = "查询userid对应角色信息")
//    @GetMapping("/userList")
//    public List<AuthRole> listAll(@RequestParam String userName, @RequestParam String type) {
//        return authRoleService.getRolesByUserName(userName,type);
//    }

}
