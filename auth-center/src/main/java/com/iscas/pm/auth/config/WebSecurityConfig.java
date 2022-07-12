package com.iscas.pm.auth.config;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

//@Configuration
//@EnableWebSecurity
public class WebSecurityConfig extends WebSecurityConfigurerAdapter {

    @Autowired
    private UserDetailsServiceImpl userDetailsService;

    @Bean
    public BCryptPasswordEncoder passwordEncoder(){
        return new BCryptPasswordEncoder();
    }

    @Override
    public void configure(AuthenticationManagerBuilder auth) throws Exception {
        auth.userDetailsService(userDetailsService).passwordEncoder(passwordEncoder());
    }

    @Override
    protected void configure(HttpSecurity http) throws Exception {

        http.authorizeRequests()
                .anyRequest().authenticated()
                .and()
                .formLogin()
                .loginProcessingUrl("/login")
                .permitAll()
                .and()
                .csrf()
                .disable();
    }

    //AuthenticationManager对象在OAuth2认证服务中要使用，提前放入IOC容器中
    @Override
    @Bean
    public AuthenticationManager authenticationManagerBean() throws Exception {
        return super.authenticationManagerBean();
    }

}


//@Configuration
//@EnableWebSecurity
//@Order(-1)                  //容器加载bean的顺序
//class WebSecurityConfig extends WebSecurityConfigurerAdapter {
//
//    /***
//     * 采用BCryptPasswordEncoder对密码进行编码
//     * @return
//     */
//    @Bean
//    public PasswordEncoder passwordEncoder() {
//        return new BCryptPasswordEncoder();
//    }
//
//    /***
//     * 忽略安全拦截的URL
//     * @param web
//     * @throws Exception
//     */
//    @Override
//    public void configure(WebSecurity web) throws Exception {
//        web.ignoring().antMatchers("/user/login","/auth/login","/login");
//    }
//
//    /***
//     * 创建授权管理认证对象
//     * @return
//     * @throws Exception
//     */
//    @Bean
//    @Override
//    public AuthenticationManager authenticationManagerBean() throws Exception {
//        return super.authenticationManagerBean();
//    }
//
//    @Override
//    protected void configure(AuthenticationManagerBuilder auth) throws Exception {
//        super.configure(auth);
//    }
//
//
//
//    /****
//     *
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
////        http.csrf().disable()
////                .httpBasic()        //启用Http基本身份验证
////                .and()
////                .formLogin()       //启用表单身份验证
////                .and()
////                .authorizeRequests()    //限制基于Request请求访问
////                .anyRequest()
////                .authenticated();       //其他请求都需要经过验证
//
//        http.authorizeRequests()
//                .anyRequest().authenticated()
//                .and()
//                .formLogin()
//                .loginProcessingUrl("/login")
//                .permitAll()
//                .and()
//                .csrf()
//                .disable();
//
//    }


//    /****
//     *
//     * @param http
//     * @throws Exception
//     */
//    @Override
//    public void configure(HttpSecurity http) throws Exception {
//        http.authorizeRequests() //安全请求策略，方法可有多个子节点matcher，每个matcher按照其声明顺序执行
//                //页面访问权限控制说明
//                .antMatchers("/").permitAll() //允许任何用户访问/路径(这个路径得有页面，否则后报404错误)
//                .antMatchers("/admin/**").hasRole("ADMIN") //以/admin/开头的URL只能由拥有ROLE_ADMIN角色的用户访问
//                .antMatchers("/db/**").access("hasRole('ADMIN') and hasRole('DBA')") //同理以/db/开头的URL
//                // 只能由同时具有ROLE_ADMIN和ROLE_DBA的用户访问
//                .anyRequest()
//                .authenticated() //其它请求进行拦截（需要进行认证）
//                .and()
//                .formLogin() //支持基于表单的登录验证
//                .successHandler(new UserAuthenticationSuccessHandler())
//                .failureHandler(new UserAuthenticationFailureHandler())
//                .permitAll() //允许基于表单登录的所有的URL的所有用户的访问
//                .and()
//                .logout() //提供注销支持
//                .logoutUrl("/logout") //注销时触发跳转/logout页面
//                .logoutSuccessUrl("/index") //注销成功后跳转/index页面
//                .logoutSuccessHandler(new LogoutSuccessHandler(){ //匿名内部类
//                    @Override
//                    public void onLogoutSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//                        httpServletResponse.setContentType("application/json;charset=UTF-8");
//                        httpServletResponse.getWriter().println("登出成功");
//                    }
//                }) //指定后/index会被忽略，注销完成执行logoutSuccessHandler，可抛出异常
//                .invalidateHttpSession(true) //注销时让HttpSession无效
//                .permitAll() //允许任何用户访问
//                .and()
//                .csrf().disable(); //禁用CSRF保护
//        http.cors().disable();
//    }
//    /**
//     * 设置通过请求拦截（登录验证）放行后/登录成功后的处理
//     * 此处也可直接写在configure(HttpSecurity http)当做匿名内部类写法/Lambda表达式实现
//     */
//    @Component("userAuthenticationSuccessHandler")
//    public class UserAuthenticationSuccessHandler implements AuthenticationSuccessHandler {
//        private Logger logger = LoggerFactory.getLogger(UserAuthenticationSuccessHandler.class);
//
//        @Override
//        public void onAuthenticationSuccess(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Authentication authentication) throws IOException, ServletException {
//            logger.info("登录成功");
//            //设置返回Response的类型和值
//            httpServletResponse.setContentType("application/json;charset=UTF-8");
//            //转为json格式返回到前端
//            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(authentication));
//            System.out.println(SecurityContextHolder.getContext().getAuthentication().getPrincipal());
//        }
//
//    }
//
//
//    /**
//     * 设置通过请求拦截（登录验证）拦截成功后/登录失败后的处理
//     */
//    @Component("userAuthenticationFailureHandler")
//    public class UserAuthenticationFailureHandler implements AuthenticationFailureHandler {
//        private Logger logger = LoggerFactory.getLogger(UserAuthenticationFailureHandler.class);
//
//        @Override
//        public void onAuthenticationFailure(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, AuthenticationException e) throws IOException, ServletException {
//            logger.info("登录失败");
//            httpServletResponse.setStatus(HttpStatus.INTERNAL_SERVER_ERROR.value());
//            httpServletResponse.setContentType("application/json;charset=UTF-8");
//            httpServletResponse.getWriter().write(objectMapper.writeValueAsString(e));
//        }
//    }
//}
