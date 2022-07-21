package com.iscas.pm.common.core.feign;

import com.iscas.pm.common.core.config.FeignConfiguration;
import com.iscas.pm.common.core.model.UserDetailInfo;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.*;

@FeignClient(name = "auth-center", configuration = FeignConfiguration.class)
public interface UserCenterClient {

    @GetMapping(value = "/user/getUserDetails")
    UserDetailInfo getUserDetails(@RequestParam(value = "userName") String userName, @RequestParam(value = "projectId") String projectId);

}