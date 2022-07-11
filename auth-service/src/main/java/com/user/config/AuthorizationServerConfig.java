package com.user.config;

import com.user.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cloud.bootstrap.encrypt.KeyProperties;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.oauth2.config.annotation.configurers.ClientDetailsServiceConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configuration.AuthorizationServerConfigurerAdapter;
import org.springframework.security.oauth2.config.annotation.web.configuration.EnableAuthorizationServer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerEndpointsConfigurer;
import org.springframework.security.oauth2.config.annotation.web.configurers.AuthorizationServerSecurityConfigurer;
import org.springframework.security.oauth2.provider.ClientDetailsService;
import org.springframework.security.oauth2.provider.approval.ApprovalStore;
import org.springframework.security.oauth2.provider.approval.JdbcApprovalStore;
import org.springframework.security.oauth2.provider.client.JdbcClientDetailsService;
import org.springframework.security.oauth2.provider.code.AuthorizationCodeServices;
import org.springframework.security.oauth2.provider.code.JdbcAuthorizationCodeServices;
import org.springframework.security.oauth2.provider.token.DefaultAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.TokenStore;
import org.springframework.security.oauth2.provider.token.store.JdbcTokenStore;
import org.springframework.security.oauth2.provider.token.store.JwtAccessTokenConverter;
import org.springframework.security.oauth2.provider.token.store.JwtTokenStore;
import org.springframework.security.oauth2.provider.token.store.KeyStoreKeyFactory;

import javax.annotation.Resource;
import javax.sql.DataSource;
import java.security.KeyPair;


@Configuration
@EnableAuthorizationServer
class AuthorizationServerConfig extends AuthorizationServerConfigurerAdapter {
        //数据库连接池对象
        @Autowired
        private DataSource dataSource;

        //认证业务对象
        @Autowired
        private UserService userService;

        //授权模式专用对象
        @Autowired
        private AuthenticationManager authenticationManager;

        //客户端信息来源
        @Bean
        public JdbcClientDetailsService jdbcClientDetailsService(){
            return new JdbcClientDetailsService(dataSource);
        }

        //token保存策略
        @Bean
        public TokenStore tokenStore(){
            return new JdbcTokenStore(dataSource);
        }

        //授权信息保存策略
        @Bean
        public ApprovalStore approvalStore(){
            return new JdbcApprovalStore(dataSource);
        }

        //授权码模式数据来源
        @Bean
        public AuthorizationCodeServices authorizationCodeServices(){
            return new JdbcAuthorizationCodeServices(dataSource);
        }

        //指定客户端信息的数据库来源
        @Override
        public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
            clients.withClientDetails(jdbcClientDetailsService());
        }

        //检查token的策略
        @Override
        public void configure(AuthorizationServerSecurityConfigurer security) throws Exception {

            security.allowFormAuthenticationForClients();
        }

        //OAuth2的主配置信息
        @Override
        public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
            endpoints
                    .approvalStore(approvalStore())
                    .authenticationManager(authenticationManager)
                    .authorizationCodeServices(authorizationCodeServices())
                    .tokenStore(tokenStore());
        }

    }
//
//    /***
//     * 客户端信息配置
//     * @param clients
//     * @throws Exception
//     */
//
//    @Override
//    public void configure(ClientDetailsServiceConfigurer clients) throws Exception {
//        //从数据库中加载客户端信息
////          clients.jdbc(dataSource).clients(clientDetails());
//        clients.inMemory()
//                .withClient("pmservice")          //客户端id
//                .secret("pmservice")                      //秘钥
//                .redirectUris("http://localhost")       //重定向地址
//                .accessTokenValiditySeconds(3600)          //访问令牌有效期
//                .refreshTokenValiditySeconds(3600)         //刷新令牌有效期
//                .authorizedGrantTypes(
//                        "authorization_code",          //根据授权码生成令牌
//                        "client_credentials",          //客户端认证
//                        "refresh_token",                //刷新令牌
//                        "password")                     //密码方式认证
//                .scopes("all");                         //客户端范围，名称自定义，必填
//
////                .withClient("client_id") //客户端账号
////                .authorizedGrantTypes("password","refresh_token") //授权类型（支持四种类型：授权码类型、隐藏式类型、密码类型、客户端凭证，以密码类型为例）
////                .accessTokenValiditySeconds(60*60*24) //授权有效期1天
////                .refreshTokenValiditySeconds(60*60*1) //刷新有效期1h
////                .resourceIds("rid")
////                .scopes("all") //授权作用域
//////                .secret("secretKey") //密码
////                //对密码加密
////                .secret(new BCryptPasswordEncoder().encode("secretKey"))
////                .redirectUris("http://localhost:80/spring/login"); //验证回调地址
//
//    }
//
//    /***
//     * 授权服务器端点配置
//     * @param endpoints
//     * @throws Exception
//     */
//
//    @Override
//    public void configure(AuthorizationServerEndpointsConfigurer endpoints) throws Exception {
//        endpoints.accessTokenConverter(jwtAccessTokenConverter)
//                .authenticationManager(authenticationManager)//认证管理器
//                .tokenStore(tokenStore)                       //令牌存储
//                .userDetailsService(userDetailsService);     //用户信息service
//    }
//
//    /***
//     * 授权服务器的安全配置
//     * @param oauthServer
//     * @throws Exception
//     */
//    @Override
//    public void configure(AuthorizationServerSecurityConfigurer oauthServer) throws Exception {
//        oauthServer.allowFormAuthenticationForClients()
//                .passwordEncoder(new BCryptPasswordEncoder())
//                .tokenKeyAccess("permitAll()")
//                .checkTokenAccess("isAuthenticated()");
//    }
//
//
//    //读取密钥的配置
//    @Bean("keyProp")
//    public KeyProperties keyProperties(){
//        return new KeyProperties();
//    }
//
//    @Resource(name = "keyProp")
//    private KeyProperties keyProperties;
//
//    //客户端配置
//    @Bean
//    public ClientDetailsService clientDetails() {
//        return new JdbcClientDetailsService(dataSource);
//    }
//
//    @Bean
//    @Autowired
//    public TokenStore tokenStore(JwtAccessTokenConverter jwtAccessTokenConverter) {
//        return new JwtTokenStore(jwtAccessTokenConverter);
//    }
//
//    /****
//     * JWT令牌转换器
//     * @param customUserAuthenticationConverter
//     * @return
//     */
//    @Bean
//    public JwtAccessTokenConverter jwtAccessTokenConverter(CustomUserAuthenticationConverter customUserAuthenticationConverter) {
//        JwtAccessTokenConverter converter = new JwtAccessTokenConverter();
//        KeyPair keyPair = new KeyStoreKeyFactory(
//                keyProperties.getKeyStore().getLocation(),                          //证书路径
//                keyProperties.getKeyStore().getSecret().toCharArray())              //证书秘钥
//                .getKeyPair(
//                        keyProperties.getKeyStore().getAlias(),                     //证书别名
//                        keyProperties.getKeyStore().getPassword().toCharArray());   //证书密码
//        converter.setKeyPair(keyPair);
//        //配置自定义的CustomUserAuthenticationConverter
//        DefaultAccessTokenConverter accessTokenConverter = (DefaultAccessTokenConverter) converter.getAccessTokenConverter();
//        accessTokenConverter.setUserTokenConverter(customUserAuthenticationConverter);
//        return converter;
//    }
//}

