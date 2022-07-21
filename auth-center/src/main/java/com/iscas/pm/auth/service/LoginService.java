//package com.iscas.pm.auth.service;
//
//import com.alibaba.fastjson.JSONObject;
//import com.iscas.pm.auth.domain.user.User;
//
//import javax.servlet.http.HttpServletResponse;
//import java.util.List;
//
//public interface LoginService {
//
//    String ssoLogin(String username, String password);
//
//    User login(String username, String password, HttpServletResponse response);
//
//    User loginByApp(String appId, long time, long num, String sig, String appTicket);
//
//    boolean logout(String token);
//
//    User check(String token, Boolean refreshExpiredTime, String type);
//
//    void removeAppIdToken(List<String> appIds);
//
//    JSONObject loginByUser(String appId, long time, long num, String sig, String loginKey);
//
//}
