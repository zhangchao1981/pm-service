package com.iscas.pm.auth.controller;

import com.baomidou.mybatisplus.core.metadata.IPage;
import com.iscas.pm.auth.domain.AuthPermission;
import com.iscas.pm.auth.domain.user.User;
import com.iscas.pm.auth.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @author by  lichang
 * @date 2022/7/18.
 */

@Slf4j
@RestController
@RequestMapping(value = "permission")
public class PermissionController {
    @Autowired
    AuthRolePermissionService authRolePermissionService;

    @Autowired
    PmRolePermissionService pmRolePermissionService;

    @Autowired
    AuthPermissionService  authPermissionService;


    //查询系统所有的权限
    @ApiOperation(value = "权限列表",notes = "分页返回系统")
    @GetMapping("/permissionList")
    @PreAuthorize("hasAuthority('/user/permissionList')")
    public List<AuthPermission> listAll() {
        return  authPermissionService.list();
    }

    //添加权限
    @ApiOperation(value = "添加权限")
    @GetMapping("/addpermission")
    @PreAuthorize("hasAuthority('/user/addpermission')")
    public  boolean addpermission(@RequestBody  AuthPermission authPermission) {
       return authPermissionService.save(authPermission);
    }

//    @ApiOperation(value = "根据userId和projectId查询pm表中的permission")
//    @PostMapping(value = "getPermissionByUserIdandProjectId")
//    public List<String> getPermissionsByUserIdandProjectId(@Valid Integer userId, @Valid Integer projectId) {
//        return pmRolePermissionService.getPermissionsByUserIdandProjectId(userId, projectId);
//    }

    @ApiOperation(value = "根据userId查询auth表中的permission")
    @PostMapping(value = "getPermissionByUserId")
    public List<String> getPermissionsByUserId(@Valid Integer userId) {
        return authRolePermissionService.getPermissionsByUserId(userId);
    }
}
