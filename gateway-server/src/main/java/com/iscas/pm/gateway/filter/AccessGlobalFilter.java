//package com.word.filter;
//
//
//import com.word.feign.AuthServerClient;
//import lombok.extern.slf4j.Slf4j;
//import org.apache.commons.lang.StringUtils;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.configurationprocessor.json.JSONObject;
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpCookie;
//import org.springframework.http.HttpHeaders;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.ResponseCookie;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.stereotype.Component;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
//import java.io.UnsupportedEncodingException;
//import java.net.InetSocketAddress;
//import java.time.Duration;
//import java.util.Objects;
//
///**
// * 网关过滤器，校验token是否有效
// **/
//@Component
//@Slf4j
//public class AccessGlobalFilter implements GlobalFilter, Ordered {
//
//    @Autowired
//    private AuthServerClient authServerClient;
//
//    @Value("${ignores}")
//    private String[] ignores;
//
//    private String[] ignoreRefreshTokenUrl = new String[]{
//            "/event/events",
//            "/organizationTree/getCurrentValue",
//            "/WaterSystem/getCulValue",
//            "/alarm/getAlarmBoard",
//            "/organizationTree/getSitesStatistics",
//            "/equipmentConstructionOfRatioAndQuantity",
//            "/typeOfRatioNumberOfEquipmentSystem"};
//
//    private String ipPattern = "^(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|[1-9])\\."
//
//            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
//
//            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)\\."
//
//            + "(1\\d{2}|2[0-4]\\d|25[0-5]|[1-9]\\d|\\d)$";
//
//
//    private String COOKIE_LOGIN = "loginFlag";
//    private String[] IGNORE_DOMAIN_ARRAY = new String[]{"flink.gridsum.com", "nifi1.gridsum.com", "nifi2.gridsum.com", "nifi3.gridsum.com"};
//
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        ServerHttpRequest request = exchange.getRequest();
//        String path = request.getURI().getPath();
//        long in = System.currentTimeMillis();
//        //当访问域名为flink.* 且已经登录的情况下,会路由到flink页面
//        HttpHeaders headers = request.getHeaders();
//        InetSocketAddress host = headers.getHost();
//        HttpCookie loginFlag = request.getCookies().getFirst(COOKIE_LOGIN);
//        for (String ignoreHost : IGNORE_DOMAIN_ARRAY) {
//            if (loginFlag != null && ignoreHost.equalsIgnoreCase(host.getHostName())) {
//                return chain.filter(exchange);
//            }
//        }
//
//        //跳过不需要登录可直接访问的接口
//        if (ignoreTokenCheck(path))
//            return chain.filter(exchange);
//
//        //获取token
//        String token = getToken(request);
//        if (StringUtils.isBlank(token) || token.equals("null")) {
//            exchange.getResponse().setStatusCode(HttpStatus.UNAUTHORIZED);
//            return exchange.getResponse().setComplete();
//        }
//
//        //调用auth-server接口，验证token是否有效
//        long inAuthCheck = System.currentTimeMillis();
//        BaseResponseBody<User> body = authServerClient.check(token, refreshTokenExpiredTime(path), token.split("_")[1].length() != 17 ? "user" : "app");
//        log.debug("{}接口authCheck完成用时：【{}ms】", path, System.currentTimeMillis() - inAuthCheck);
//        if (body.getCode() != 200)
//            throw new AuthenticateException(body.getMessage());
//
//        User user = body.getData();
//
//        //补全header信息（userId,userName,databaseName,fromIP）
//        if (StringUtils.isNotBlank(user.getCurrentDataBase())) {
//            request.mutate().header("databaseName", user.getCurrentDataBase()).build();
//        }
//        log.debug("{}getCurrentDataBase()完成用时：【{}ms】", path, System.currentTimeMillis() - inAuthCheck);
//        if (user.getUserId() != null && StringUtils.isNotBlank(user.getUserId().toString())) {
//            request.mutate().header("userId", user.getUserId().toString()).build();
//            log.debug("{}getUserId()完成用时：【{}ms】", path, System.currentTimeMillis() - inAuthCheck);
//        }
//        if (StringUtils.isNotBlank(user.getUserName())) {
//            try {
//                request.mutate().header("userName", URLEncoder.encode(user.getUserName(), "UTF-8")).build();
//                log.debug("{}getUserName()完成用时：【{}ms】", path, System.currentTimeMillis() - inAuthCheck);
//            } catch (UnsupportedEncodingException e) {
//                log.error("解码异常：{}", e.getMessage(), e);
//            }
//        }
//
//        //设置cookie  flink登录
//        if (!host.getHostName().matches(ipPattern) && loginFlag == null) {
//            String domain = host.getHostName();
//            if (domain.indexOf(".") > 0) {
//                domain = domain.substring(domain.indexOf("."));
//            }
//            ResponseCookie cookie = ResponseCookie.from(COOKIE_LOGIN, Boolean.TRUE.toString())
//                    .domain(domain)
//                    .path("/")
//                    .maxAge(Duration.ofMinutes(30)).build();
//            log.info("设置cookie {}", JSONObject.toJSONString(cookie));
//            exchange.getResponse().addCookie(cookie);
//        }
//
//        log.debug("{}接口在网关完成认证用时：【{}ms】", path, System.currentTimeMillis() - in);
//        return chain.filter(exchange);
//    }
//
////    private ServerHttpRequest removeDefaultRequestHeader(ServerHttpRequest request){
////        try{
////            Field connectorField = ReflectionUtils.findField(RequestFacade.class, "request", Request.class);
////            connectorField.setAccessible(true);
////            Request connectorRequest = (Request) connectorField.get(request);
////
////            // 从 org.apache.catalina.connector.Request 中获取 org.apache.coyote.Request
////            Field coyoteField = ReflectionUtils.findField(Request.class, "coyoteRequest", org.apache.coyote.Request.class);
////            coyoteField.setAccessible(true);
////            org.apache.coyote.Request coyoteRequest = (org.apache.coyote.Request) coyoteField.get(connectorRequest);
////
////            // 从 org.apache.coyote.Request 中获取 MimeHeaders
////            Field mimeHeadersField =  ReflectionUtils.findField(org.apache.coyote.Request.class, "headers", MimeHeaders.class);
////            mimeHeadersField.setAccessible(true);
////            MimeHeaders mimeHeaders =  (MimeHeaders) mimeHeadersField.get(coyoteRequest);
////
////            mimeHeaders.removeHeader("userId");
////            mimeHeaders.removeHeader("databaseName");
////            mimeHeaders.removeHeader("userName");
////        }catch (Exception e){
////            log.error("移除请求头中的字段失败:",e);
////        }
////        return request;
////    }
//    @Override
//    public int getOrder() {
//        return 6;
//    }
//
//    @SuppressWarnings({"squid:S3358", "squid:S3776"})
//    private String getToken(ServerHttpRequest request) {
//        //从header中获取token
//        String token = request.getHeaders().getFirst(AuthConstants.AUTHORIZATION_HEADER);
//        boolean fromHeader = true;
//
//        //从Cookie中获取token
//        if (StringUtils.isBlank(token)) {
//            String cookie = request.getHeaders().getFirst(HttpHeaders.COOKIE);
//            if (cookie != null) {
//                String[] cookies = cookie.split(";");
//                for (String item : cookies) {
//                    if (item.trim().startsWith(AuthConstants.AUTHORIZATION_HEADER)) {
//                        token = item.split("=")[1];
//                        fromHeader = false;
//                        break;
//                    }
//                }
//            }
//        }
//
//        //从parameter中获取token
//        if (StringUtils.isBlank(token)) {
//            token = request.getQueryParams().containsKey(AuthConstants.AUTHORIZATION_HEADER) ?
//                    request.getQueryParams().getFirst(AuthConstants.AUTHORIZATION_HEADER) :
//                    request.getQueryParams().containsKey("token") ?
//                            request.getQueryParams().getFirst("token") : null;
//            fromHeader = false;
//        }
//
//        if (!fromHeader && StringUtils.isNotBlank(token))
//            request.mutate().header(AuthConstants.AUTHORIZATION_HEADER, token);
//
//        return token;
//    }
//
//    private boolean ignoreTokenCheck(String path) {
//        for (String s : ignores) {
//            if (path.contains(s) || s.contains(path)) {
//                return true;
//            }
//        }
//        return false;
//    }
//
//    private boolean refreshTokenExpiredTime(String path) {
//        if (Objects.isNull(path))
//            return true;
//
//        for (String s : ignoreRefreshTokenUrl) {
//            if (s.contains(path) || path.contains(s))
//                return false;
//        }
//        return true;
//    }
//
//}
