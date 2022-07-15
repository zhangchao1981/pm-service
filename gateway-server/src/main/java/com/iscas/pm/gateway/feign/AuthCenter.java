package com.iscas.pm.gateway.feign;

import com.iscas.pm.common.core.web.response.BaseResponse;
import com.iscas.pm.common.core.model.User;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@FeignClient("auth-center")
public interface AuthCenter {

    @GetMapping("/auth/token/{token}")
    BaseResponse<User> check(@PathVariable("token") String token);
}
