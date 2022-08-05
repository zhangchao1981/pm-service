package com.iscas.pm.auth.controller;

import com.iscas.pm.auth.model.Permission;
import com.iscas.pm.auth.service.*;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.Date;
import java.util.List;

/**
 * @Author： lichang
 * @Date： 2022/7/27
 * @Description： 权限管理控制类
 */
@Slf4j
@RestController
@RequestMapping(value = "permission")
@Api(tags = {"权限管理"})
public class PermissionController {

    @Autowired
    PermissionService permissionService;

    @ApiOperation(value = "权限列表",notes = "返回权限列表")
    @GetMapping("/permissionList")
    @PreAuthorize("hasAuthority('/permission/permissionList')")
    public List<Permission> permissionList() {
        return  permissionService.list();
    }

    @ApiOperation(value = "添加权限",notes = "添加新权限")
    @PostMapping("/addPermission")
    @PreAuthorize("hasAuthority('/permission/addPermission')")
    public  boolean addPermission(@RequestBody @Valid Permission permission) {
        if (permissionService.getById(permission.getId()) != null) {
            throw new IllegalArgumentException("权限标识已经存在，不能重复添加");
        }
        permission.setCreateTime(new Date());
        permission.setUpdateTime(new Date());
       return permissionService.save(permission);
    }

    @ApiOperation(value = "编辑权限", notes = "编辑指定的权限信息")
    @PostMapping("editPermission")
    @PreAuthorize("hasAuthority('/permission/editPermission')")
    public Boolean editPermission(@Valid @RequestBody Permission permission) {
        if (permissionService.getById(permission.getId()) == null) {
            throw new IllegalArgumentException("权限不存在");
        }
        permissionService.saveOrUpdate(permission);
        return true;
    }

    @ApiOperation(value = "删除权限")
    @PostMapping("deletePermission")
    @PreAuthorize("hasAuthority('/permission/deletePermission')")
    public Boolean deletePermission(@NotBlank @RequestParam String permissionId) {
        if (permissionService.getById(permissionId) == null) {
            throw new IllegalArgumentException("权限不存在");
        }

        permissionService.removeById(permissionId);
        return true;
    }

}
