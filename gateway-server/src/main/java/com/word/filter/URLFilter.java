package com.word.filter;//package com.word.filter;
//
///**
// * created By lichang on
// */
//
///**
// * 不需要认证就能访问的路径校验
// *
// * 校验当前访问路径是否需要验证权限
// * 如果不需要验证：false
// * 如果需要验证:   true
// */
//public class URLFilter {
//
//    private static final String noAuthorizeurls = "/api/user/add,/api/user/login";   //有毛病这里  http://localhost:8001/api/user/login被识别不在可放行的url里
//    /**
//     * @param uri 获取到的当前的请求的地址
//     * @return
//     */
//    public static boolean hasAuthorize(String uri) {
//        //不需要拦截的URL
//        String[] urls = noAuthorizeurls.split(",");
//        for (String s : urls) {
//            if (s.equals(uri)) {
//                return false;
//            }
//        }
//        return true;
//    }
//
//
//}