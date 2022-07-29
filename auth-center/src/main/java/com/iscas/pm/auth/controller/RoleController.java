package com.iscas.pm.auth.controller;

import com.iscas.pm.auth.domain.Permission;
import com.iscas.pm.auth.domain.Role;
import com.iscas.pm.auth.domain.RoleTypeEnum;
import com.iscas.pm.auth.service.RoleService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiImplicitParams;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.Date;
import java.util.List;

/**
 * @Author： lichang
 * @Date： 2022/7/27
 * @Description： 角色管理控制类
 */
@Slf4j
@RestController
@RequestMapping("/role")
@Api(tags = {"角色管理"})
public class RoleController {
    @Autowired
    RoleService roleService;

    @ApiOperation(value = "角色列表", notes = "返回符合条件的角色列表,包括系统角色和项目角色")
    @GetMapping("/roleList")
    @PreAuthorize("hasAuthority('/role/roleList')")
    @ApiImplicitParams({
            @ApiImplicitParam(name = "name", value = "角色名称", required = false, dataType = "String"),
            @ApiImplicitParam(name = "type", value = "角色类型", required = false, dataType = "String")})
    public List<Role> roleList(@RequestParam String name, @RequestParam String type) {
        return roleService.selectRoleList(name, type);
    }

    @ApiOperation(value = "添加角色")
    @PostMapping("addRole")
    @PreAuthorize("hasAuthority('/role/addRole')")
    public Role addRole(@Valid @RequestBody Role role) {
        if (roleService.existRole(null, role.getName()))
            throw new IllegalArgumentException("角色名称已经存在");

        role.setCreateTime(new Date());
        role.setUpdateTime(new Date());
        roleService.save(role);
        return role;
    }

    @ApiOperation(value = "编辑角色", notes = "编辑角色信息")
    @PostMapping("editRole")
    @PreAuthorize("hasAuthority('/role/editRole')")
    public Boolean editRole(@Valid @RequestBody Role role) {
        if (role.getId() == null)
            throw new IllegalArgumentException("角色id不能为空");

        if (roleService.getById(role.getId()) == null) {
            throw new IllegalArgumentException("待删除的角色不存在");
        }

        //重名校验
        if (roleService.existRole(role.getId(), role.getName()))
            throw new IllegalArgumentException("角色名称已经存在");

        roleService.saveOrUpdate(role);
        return true;
    }

    @ApiOperation(value = "删除角色")
    @PostMapping("deleteRole")
    @PreAuthorize("hasAuthority('/role/deleteRole')")
    public Boolean deleteRole(@NotBlank @RequestParam String roleId) {
        Role role = roleService.getById(roleId);
        if (role == null) {
            throw new IllegalArgumentException("待删除的角色不存在");
        }

        roleService.removeById(roleId);

        //todo 缺少清空token信息，待开发
        return true;
    }

    @ApiOperation(value = "分配权限", notes = "为指定角色分配权限")
    @PostMapping("settingPermissions")
    @PreAuthorize("hasAuthority('/role/settingPermissions')")
    public Boolean settingPermissions(@NotNull @RequestParam Integer roleId, @RequestBody List<String> permissionIds) {
        Role role = roleService.getById(roleId);
        if (role == null) {
            throw new IllegalArgumentException("角色不存在");
        }

        return roleService.addRolePermissions(roleId, permissionIds);
    }


    @ApiOperation(value = "查询系统角色列表", notes = "返回所有系统角色列表,给用户分配系统角色时使用")
    @GetMapping("/systemRoleList")
    @PreAuthorize("hasAuthority('/user/settingRoles')")
    public List<Role> systemRoleList() {
        return roleService.selectRoleList(null, RoleTypeEnum.system.name());
    }

    @ApiOperation(value = "查询角色已分配的权限列表", notes = "查询指定角色已分配的权限列表，给角色分配权限的时候使用")
    @PostMapping("getRolePermissions")
    @PreAuthorize("hasAuthority('/role/settingPermissions')")
    public List<Permission> getRolePermissions(@NotNull @RequestParam Integer roleId) {
        Role role = roleService.getById(roleId);
        if (role == null) {
            throw new IllegalArgumentException("角色不存在");
        }

        return roleService.getRolePermissions(roleId);
    }

}
