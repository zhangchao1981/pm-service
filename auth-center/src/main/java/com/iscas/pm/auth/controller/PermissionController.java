package com.iscas.pm.auth.controller;

import com.iscas.pm.auth.service.*;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

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

    @ApiOperation(value = "根据userId和projectId查询pm表中的permission")
    @PostMapping(value = "getPermissionByUserIdandProjectId")
    public List<String> getPermissionsByUserIdandProjectId(@Valid Integer userId, @Valid Integer projectId) {
        return pmRolePermissionService.getPermissionsByUserIdandProjectId(userId, projectId);
    }

    @ApiOperation(value = "根据userId查询auth表中的permission")
    @PostMapping(value = "getPermissionByUserId")
    public List<String> getPermissionsByUserId(@Valid Integer userId) {
        return authRolePermissionService.getPermissionsByUserId(userId);
    }
}
