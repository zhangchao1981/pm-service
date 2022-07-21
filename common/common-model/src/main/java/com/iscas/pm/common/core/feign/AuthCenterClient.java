package com.iscas.pm.common.core.feign;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.*;

@FeignClient("auth-center")
public interface AuthCenterClient {

    @GetMapping(value = "/user/getUserDetails")
    UserDetails getUserDetails(@RequestHeader("userId")  String userId, @RequestHeader("projectId") String projectId,@RequestHeader("Authorization") String token);

}