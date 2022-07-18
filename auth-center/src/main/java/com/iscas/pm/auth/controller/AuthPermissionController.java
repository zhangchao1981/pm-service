package com.iscas.pm.auth.controller;

import com.iscas.pm.auth.service.AuthPermissionService;
import com.iscas.pm.common.core.util.Wrapper;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * @author by  lichang
 * @date 2022/7/18.
 */
@Wrapper
@Slf4j
@RestController
@RequestMapping(value = "table_permission")
public class AuthPermissionController {
@Autowired
private AuthPermissionService authPermissionService;

@RequestMapping(value = "permissionName")
public String findPermissionNameById(String permissionId){
    return authPermissionService.findPermissionNameById(permissionId);

}
}
