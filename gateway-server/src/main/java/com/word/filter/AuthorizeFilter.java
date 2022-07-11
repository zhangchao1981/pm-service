package com.word.filter;//package com.word.filter;
//
//import org.springframework.cloud.gateway.filter.GatewayFilterChain;
//import org.springframework.cloud.gateway.filter.GlobalFilter;
//import org.springframework.core.Ordered;
//import org.springframework.http.HttpCookie;
//import org.springframework.http.HttpStatus;
//import org.springframework.http.server.reactive.ServerHttpRequest;
//import org.springframework.http.server.reactive.ServerHttpResponse;
//import org.springframework.stereotype.Component;
//import org.springframework.util.StringUtils;
//import org.springframework.web.server.ServerWebExchange;
//import reactor.core.publisher.Mono;
//
///**
// * 全局过滤器 :用于鉴权(获取令牌 解析 判断)
// *
// * @author 李昶
// * @version 1.0
// * @package com.changgou.filter *
// * @since 1.0
// */
//@Component
//public class AuthorizeFilter implements GlobalFilter, Ordered {
//    //令牌的名字
//    private static final String AUTHORIZE_TOKEN = "Authorization";
//
//    /**
//     * 全局拦截
//     *
//     * @param exchange
//     * @param chain
//     * @return
//     */
//    @Override
//    public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
//        //1.获取请求对象
//        ServerHttpRequest request = exchange.getRequest();
//        //2.获取响应对象
//        ServerHttpResponse response = exchange.getResponse();
//        //用户如果是登录(访问认证微服务获取令牌)或者一些不需要做权限认证的请求，直接放行
//        String uri = request.getURI().toString();
////        Boolean fffftest=URLFilter.hasAuthorize(uri);
//        if(!URLFilter.hasAuthorize(uri)){
//            return chain.filter(exchange);
//        }
//
//
//        //获取用户令牌信息   1)头文件   2)参数获取 3)Cookie中
//        //如果没有令牌，则拦截
//        //如果有令牌，则校验令牌是否有效
//        //无效则拦截
//        //有效方形
//        //3.判断 是否为登录的URL 如果是 放行
//        if (request.getURI().getPath().startsWith("/api/user/login")) {
//            return chain.filter(exchange);
//        }
//        //4.判断 是否为登录的URL 如果不是      权限校验
//
//        //4.1 从头header中获取令牌数据
//        String token = request.getHeaders().getFirst(AUTHORIZE_TOKEN);
//        boolean hasToken = true;
//        if (StringUtils.isEmpty(token)) {
//            //4.2 从请求参数中获取令牌数据
//            token = request.getQueryParams().getFirst(AUTHORIZE_TOKEN);
//            hasToken = false;//进入这个if就说明请求头里的令牌为空了，无论参数里有没有，都要设hasToken=false
//        }
//        //这样就从参数里也找一次令牌，如果还为空，就说明参数里也没有
//        if (StringUtils.isEmpty(token)) {
//            //4.3 从cookie中中获取令牌数据
//            HttpCookie first = request.getCookies().getFirst(AUTHORIZE_TOKEN);
//            if (first != null) {
//                token = first.getValue();//就是令牌的数据
//            }
//        }
//
////        如果令牌为空，则不允许访问，直接拦截
//        if (StringUtils.isEmpty(token)) {
//            response.setStatusCode(HttpStatus.UNAUTHORIZED);
//            //响应空数据
//            return response.setComplete();
//        } else {  //也就是token不为空
//            //如果请求头中没有令牌  这时候再把令牌封装到头里，若不管有没有都给头里封装令牌，头里面一个key允许对应多个值，就会重复添加多个令牌
//            if (!hasToken) {
//                //将令牌封装到头文件中
//                //判断当前令牌是否有bearer前缀，如果没有，则添加前缀 bearer  （也有可能是大写的）  因为此时这个token可能不是从请求头里获取的，有可能不带前缀
//                if (!token.startsWith("bearer ") && !token.startsWith("Bearer ")) {
//                    token = "bearer " + token;
//                }
//                request.mutate().header(AUTHORIZE_TOKEN, token);
//            }
//        }
//        //有效，放行
//        return chain.filter(exchange);
//    }
//
//    @Override
//    public int getOrder() {
//        return 0;
//    }
//}
