package com.iscas.pm.auth.controller;

import com.iscas.pm.auth.domain.AuthPermission;
import com.iscas.pm.auth.domain.AuthRole;
import com.iscas.pm.auth.domain.AuthUserRole;
import com.iscas.pm.auth.mapper.PmProjectUserRoleMapper;
import com.iscas.pm.auth.service.*;
import com.iscas.pm.common.core.util.Wrapper;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

/**
 * @author by  lichang
 * @date 2022/7/18.
 */

@Wrapper
@Slf4j
@RestController
@RequestMapping(value = "permission")
public class PermissionController {
    @Autowired
    AuthRolePermissionService authRolePermissionService;
    @Autowired
    PmRolePermissionService pmRolePermissionService;
    /**
     * @return
     */
    @ApiOperation(value = "根据userId和projectId查询pm表中的permission")
    @PostMapping(value = "getPermissionByUserIdandProjectId")
    public List<String> getPermissionsByUserIdandProjectId(Integer userId, Integer projectId) {
/**
 *
 SELECT pm_role_permission.permission_id from pm_role_permission
 INNER JOIN pm_project_user_role
 ON pm_role_permission.role_id=pm_project_user_role.role_id
 where  pm_project_user_role.project_id=1 AND pm_project_user_role.user_id=1;
 */
        return pmRolePermissionService.getPermissionsByUserIdandProjectId(userId, projectId);
    }


    @ApiOperation(value = "根据userId查询auth表中的permission")
    @PostMapping(value = "getPermissionByUserId")
    public List<String> getPermissionsByUserId(Integer userId) {
/**
 *  * select auth_role_permission.permission_id  from auth_role_permission  INNER JOIN auth_user_role ON auth_role_permission.role_id= auth_user_role.role_id where  auth_user_role.user_id=1;
 */
        return authRolePermissionService.getPermissionsByUserId(userId);
    }
}
