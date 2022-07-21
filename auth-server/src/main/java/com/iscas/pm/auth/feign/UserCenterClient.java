package com.iscas.pm.auth.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "auth-center")
public interface UserCenterClient {

    @GetMapping(value = "/user/getUserDetails")
    UserDetailInfo getUserDetails(@RequestParam String userName, @RequestParam String projectId);

}