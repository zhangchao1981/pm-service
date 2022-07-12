//package com.word.feign;
//
//import com.energy.model.BaseResponseBody;
//import com.energy.model.auth.User;
//import org.springframework.cloud.openfeign.FeignClient;
//import org.springframework.web.bind.annotation.GetMapping;
//import org.springframework.web.bind.annotation.PathVariable;
//import org.springframework.web.bind.annotation.RequestParam;
//
//@FeignClient("auth-server")
//public interface AuthServerClient {
//
//    @GetMapping("/auth/token/{token}")
//    BaseResponseBody<User> check(@PathVariable("token") String token, @RequestParam(name = "refreshExpiredTime", required = false) Boolean refreshExpiredTime,@RequestParam("type")String type);
//}
