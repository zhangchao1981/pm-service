package com.iscas.pm.api.service.impl;

import com.iscas.pm.api.model.project.SettingSystemRoleQueryParam;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import javax.validation.Valid;

/**
 * @author by  lichang
 * @date 2022/9/27.
 */
@FeignClient(name="addRole",fallback=Boolean.class)
public interface UserService {
    @RequestMapping(value="/user/settingSystemRole",method = RequestMethod.POST)
    Boolean settingSystemRole(@Valid @RequestBody SettingSystemRoleQueryParam queryParam);
}
