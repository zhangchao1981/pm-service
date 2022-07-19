package com.iscas.pm.auth.controller;

import com.iscas.pm.auth.service.AuthUserRoleService;
import com.iscas.pm.common.core.util.Wrapper;
import io.swagger.annotations.Api;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by  lichang
 * @date 2022/7/19.
 */

@Wrapper
@Slf4j
@RestController
@RequestMapping("/roleallpermission")
@Api(tags = {"用户管理"})
public class RolePermissionController {
    @Autowired
    private AuthUserRoleService authUserRoleService;

    @RequestMapping("/roleallpermission")
    public




}
