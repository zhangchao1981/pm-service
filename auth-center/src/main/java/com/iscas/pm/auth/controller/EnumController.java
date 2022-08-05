package com.iscas.pm.auth.controller;

import com.iscas.pm.auth.domain.RoleTypeEnum;
import com.iscas.pm.auth.domain.UserStatusEnum;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.web.bind.annotation.*;

/**
 * @author zhangchao
 * @Date: 20122/7/14 16:42
 * @Description: 枚举类型api
 */
@RestController
@RequestMapping(value = "/enum")
@Api(tags = {"枚举项列表"})
public class EnumController {

    @ApiOperation(value = "用户状态",notes = "获取用户状态枚举项列表")
    @PostMapping(value = "/userStatus")
    public UserStatusEnum[] userStatusEnums() {
        return UserStatusEnum.values();
    }

    @ApiOperation(value = "角色类型",notes = "获取角色类型枚举项列表")
    @PostMapping(value = "/roleTypes")
    public RoleTypeEnum[] roleTypeEnums() {
        return RoleTypeEnum.values();
    }

}
