package com.iscas.pm.auth.service.impl;

import com.alibaba.fastjson.JSONObject;
import com.energy.common.cache.RedisUtil;
import com.energy.common.databaseseparate.holder.DataSourceHolder;
import com.energy.common.event.EventService;
import com.energy.common.licensecheck.LicenseCheck;
import com.energy.common.licensecheck.LicenseType;
import com.energy.common.util.RSACoder;
import com.energy.common.web.filter.RequestHolder;
import com.energy.common.welink.util.EncryptForWelink;
import com.energy.model.BaseResponseBody;
import com.energy.model.auth.AuthToken;
import com.energy.model.auth.User;
import com.energy.model.constant.AuthConstants;
import com.energy.model.constant.RedisKeyConstants;
import com.energy.model.data.AppOnline;
import com.energy.model.device.Employee;
import com.energy.model.enums.AppState;
import com.energy.model.enums.OperateType;
import com.energy.model.exception.AuthenticateException;
import com.energy.model.iot.App;
import com.energy.model.project.Authority;
import com.energy.model.project.Project;
import com.gridsum.tac.auth.feign.AppService;
import com.gridsum.tac.auth.mapper.major.UserMapper;
import com.gridsum.tac.auth.repository.TokenRepository;
import com.gridsum.tac.auth.service.IoTService;
import com.gridsum.tac.auth.service.LoginService;
import com.gridsum.tac.auth.service.ProjectAuthorityService;
import com.gridsum.tac.auth.service.SsoAuthenticateService;
import com.iscas.pm.auth.domain.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
public class LoginServiceImpl implements LoginService {
    @Autowired
    private TokenRepository tokenRepository;
    @Autowired
    private MongoTemplate mongoTemplate;
    @Autowired
    private UserMapper userMapper;
    @Autowired
    private SsoAuthenticateService ssoAuthenticateService;
    @Autowired
    private IoTService ioTService;
    @Autowired
    private AppService appService;
    @Autowired
    private EventService eventService;
    @Autowired
    private RedisUtil redisUtil;
    @Autowired
    private ProjectAuthorityService projectAuthorityService;
    @Value("${sso.defaultPass}")
    private String defaultPass;
    @Value("${sso.apply}")
    private Boolean applySSO;
    @Value("${deploy.cloud}")
    private boolean isCloud;

    private static final String TOKENS = "tokens";

    @Override
    public String ssoLogin(String username, String password) {
        JSONObject data = new JSONObject();
        data.put("username", username);
        data.put("password", password);

        BaseResponseBody<String> res = ssoAuthenticateService.login(data.toJSONString());

        if (res == null || res.getCode() != 0 || !"Success".equals(res.getMessage())) {
            String message =  res != null ? res.getMessage() : "";
            // 安全检查，不可明文打印密码
            throw new IllegalArgumentException(username + "在sso认证出错,密码：" + message);
        }

        return res.getData();
    }

    @Override
    @SuppressWarnings("java:S3776")
    public User login(String username, String password, HttpServletResponse response) {
        User user = userMapper.selectByUsername(username);
        if (user == null)
            recordRetryCountToRedis(username);

        if (user.getStatus() == 0)
            throw new IllegalArgumentException("该账号已禁用，请联系管理员");

        if (!StringUtils.isBlank(user.getFailureTime()) && !Objects.equals("0", user.getFailureTime())) {
            LocalDateTime failureTime = LocalDateTime.parse(user.getFailureTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
            if (LocalDateTime.now().compareTo(failureTime) > 0)
                throw new IllegalArgumentException("该账号已失效，请联系管理员");
        }

        //本地认证
        if (!applySSO && !Objects.equals(RSACoder.decryptByPrivateKey(password), RSACoder.decryptByPrivateKey(user.getPassword()))) {
            recordRetryCountToRedis(username);
        }

        //sso认证，生成token和loginKey（第三方应用单点登录使用）
        String token = "";
        if (applySSO) {
            try {
                token = ssoLogin(username, RSACoder.decryptByPrivateKey(password));
            } catch (IllegalArgumentException ex) {
                if (!ex.getMessage().contains("Username or password is wrong"))
                    throw new AuthenticateException();
                recordRetryCountToRedis(username);
            }
        } else {
            token = UUID.randomUUID().toString().replace("-", "");
        }
        token = token + AuthToken.TOKEN_SEPARATOR + user.getUserId();
        user.setLoginKey(UUID.randomUUID().toString().replace("-", "").substring(0, 7));

        //存储token信息到mongodb
        AuthToken authToken = new AuthToken();
        String fromIP = !Objects.isNull(RequestHolder.getUser()) ? RequestHolder.getUser().getFromIP() : "";
        long expiredMills = System.currentTimeMillis() + user.getLogoutTime() * 60 * 1000L;
        authToken
                .setUserName(user.getUserName())
                .setToken(token)
                .setCreateTime(new Date())
                .setExpiredTime(user.getLogoutTime() == 0 ? new Date(2099, 1, 1) : new Date(expiredMills))
                .setFromIP(StringUtils.isEmpty(fromIP) ? "未知ip" : fromIP)
                .setLoginKey(user.getLoginKey());
        DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
        tokenRepository.save(authToken);

        //切换到默认项目
        switchToDefaultProject(token, user.getUserId());

        if (response != null) {
              // 安全问题，暂时注释
//            //token放入cookie（方便swagger调用）
//            Cookie cookie = new Cookie("Authorization", token);
//            cookie.setPath("/");
//            cookie.setMaxAge(3 * 24 * 3600);//设置Cookie的有效期(3天)
//            response.addCookie(cookie);
        }

        user.setChangePassWord(Objects.equals(RSACoder.decryptByPrivateKey(password), defaultPass));
        user.setToken(token);
        user.setPassword(null);
        return user;
    }

    @Override
    public User loginByApp(String appId, long time, long num, String sig, String appTicket) {
        BaseResponseBody<JSONObject> res = ioTService.getAPPInfoByAppId(DataSourceHolder.DEFAULT_USER, appId);
        if (res == null || res.getCode() != 200 || res.getData() == null)
            throw new AuthenticateException("appId无效");

        String state = res.getData().getJSONObject("state").getString("code");
        String ticket = res.getData().getString("appTicket");
        String key = res.getData().getString("key");
        String projectId = res.getData().getString("projectId");

        if (!AppState.enable.toString().equals(state))
            throw new AuthenticateException("您的应用尚未启用");

        if (!appTicket.equals(ticket))
            throw new AuthenticateException("app_ticket无效");

        String encrypt = EncryptForWelink.encrypt(key, time, num);
        if (!encrypt.equals(sig)){
            log.error("sig错误：正确-【{}】用户提交-【{}】key：【{}】time:【{}】,num:【{}】",encrypt,sig,key,time,num);
            throw new AuthenticateException("sig错误");
        }

        //本地生成token，存入tokens表
        String token = UUID.randomUUID().toString().replace("-", "");
        AuthToken authToken = new AuthToken()
                .setAppId(appId)
                .setUserName(appId)
                .setToken(token + "_" + appId)
                .setCreateTime(new Date())
                .setExpiredTime(new Date(System.currentTimeMillis() + 60 * 24 * 60 * 1000L))
                .setCurrentDataBase(projectId);
        DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
        tokenRepository.save(authToken);

        //发布登录事件通知
        eventService.sendLoginEvent(appId, appId, Collections.singletonList(projectId), OperateType.LOGIN);

        //通知设备上线（用来检测云下ddc工具的状态，进而检测设备的上下线状态）
        if (isCloud) {
            AppOnline appOnline = new AppOnline()
                    .setAppId(appId)
                    .setProjectId(projectId)
                    .setStatus(AuthConstants.APP_ONLINE);
            appService.changeStatus(DataSourceHolder.DEFAULT_USER, appOnline);
        }
        DataSourceHolder.setDB(projectId);
        return new User().setUserName(appId).setToken(authToken.getToken());
    }

    @Override
    public JSONObject loginByUser(String appId, long time, long num, String sig, String loginKey) {
        BaseResponseBody<JSONObject> res = ioTService.getAPPInfoByAppId(DataSourceHolder.DEFAULT_USER, appId);
        if (res == null || res.getCode() != 200 || res.getData() == null)
            throw new AuthenticateException("appId无效");

        String state = res.getData().getJSONObject("state").getString("code");
        String key = res.getData().getString("key");

        if (!AppState.enable.toString().equals(state))
            throw new AuthenticateException("您的应用尚未启用");

        if (!EncryptForWelink.encrypt(key, time, num).equals(sig))
            throw new AuthenticateException("sig错误");

        DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
        AuthToken userToken = mongoTemplate.findOne(Query.query(Criteria.where("loginKey").is(loginKey)), AuthToken.class);
        if (userToken == null)
            throw new IllegalArgumentException("loginKey无效");

        String appToken = UUID.randomUUID().toString().replace("-", "") + "_" + appId;
        AuthToken authToken = new AuthToken()
                .setAppId(appId)
                .setToken(appToken)
                .setCreateTime(new Date())
                .setExpiredTime(new Date(System.currentTimeMillis() + 30 * 60 * 1000L))
                .setUserToken(userToken.getToken());
        tokenRepository.save(authToken);

        JSONObject data = new JSONObject();
        data.put("token", appToken);
        return data;
    }

    @Override
    public boolean logout(String token) {
        DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
        tokenRepository.deleteById(token);

        if (applySSO)
            ssoAuthenticateService.logout(token.substring(0, token.lastIndexOf(AuthToken.TOKEN_SEPARATOR)));

        //向所有有权限的项目发布登出事件
        String userId = RequestHolder.getUser().getUserId();
        List<Authority> authorities = projectAuthorityService.getAuthorities(userId, null, null, false);
        List<String> projectIds = authorities.stream().map(Authority::getProjectId).distinct().collect(Collectors.toList());
        if (!projectIds.isEmpty()) {
            eventService.sendLoginEvent(authorities.get(0).getUserName(), userId, projectIds, OperateType.LOGOUT);
        }
        return true;
    }

    @Override
    @SuppressWarnings("java:S3776")
    public User check(String token, Boolean refreshExpiredTime, String type) {
        long in = System.currentTimeMillis();
        //系统内部认证
        AuthToken authToken = tokenRepository.findById(token)
                .orElseThrow(() -> new AuthenticateException("token认证失败"));

        //会话过期处理
        if (System.currentTimeMillis() - authToken.getExpiredTime().getTime() > 0) {
            tokenRepository.deleteById(token);
            throw new AuthenticateException("会话已过期");
        }

        User user = new User();
        if (Objects.equals("user", type)) {
            //sso认证
            if (applySSO) {
                log.debug("token【{}】进入sso认证token前用时:【{}ms】", token, System.currentTimeMillis() - in);
                String ssoTicket = token.substring(0, token.lastIndexOf(AuthToken.TOKEN_SEPARATOR));
                BaseResponseBody<Boolean> checked = ssoAuthenticateService.validateTicket(ssoTicket);
                log.debug("token【{}】完成sso认证token后用时:【{}ms】", token, System.currentTimeMillis() - in);
                if (checked.getData() == null || !checked.getData())
                    throw new AuthenticateException("sso中token认证失败");
            }
            user.setUserId(authToken.getUserId());
            user = userMapper.selectByPrimaryKey(authToken.getUserId());
            log.debug("token【{}】查询完成用户信息后用时:【{}ms】", token, System.currentTimeMillis() - in);
            //校验用户是否已经失效
            if (!StringUtils.isBlank(user.getFailureTime()) && !Objects.equals("0", user.getFailureTime())) {
                LocalDateTime failureTime = LocalDateTime.parse(user.getFailureTime(), DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss"));
                if (LocalDateTime.now().compareTo(failureTime) > 0)
                    throw new IllegalArgumentException("该账号已失效，请联系管理员");
            }
            //更新会话过期时间
            if (!Objects.equals(user.getLogoutTime(), 0) && refreshExpiredTime != null && refreshExpiredTime) {
                mongoTemplate.updateFirst(
                        new Query(Criteria.where("_id").is(token)),
                        new Update().set("expiredTime", new Date(System.currentTimeMillis() + user.getLogoutTime() * 60 * 1000L)),
                        AuthToken.class);
                log.debug("token【{}】更新token失效时间完成后用时:【{}ms】", token, System.currentTimeMillis() - in);
            }
            if (StringUtils.isNotBlank(authToken.getUserName())) {
                user.setUserName(authToken.getUserName());
            }
        } else {
            DataSourceHolder.setDB(authToken.getCurrentDataBase());
            App app = mongoTemplate.findOne(Query.query(Criteria.where("_id").is(authToken.getAppId())), App.class);
            if (app != null) {
                user.setUserName(app.getName());
            } else {
                user.setUserName(authToken.getAppId());
            }
        }
        user.setCurrentDataBase(authToken.getCurrentDataBase());
        return user;
    }

    @Override
    public void removeAppIdToken(List<String> appIds) {
        DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
        mongoTemplate.remove(Query.query(Criteria.where("appId").in(appIds)), AuthToken.class);
    }



    private void switchToDefaultProject(String token, Integer userId) {
        //获取授权的项目列表(不包含已被禁用的项目)
        List<Authority> authorities = mongoTemplate.find(Query.query(Criteria.where("userId").is(userId.toString())), Authority.class);
        List<com.energy.model.project.Project> disabledProjects = mongoTemplate.find(Query.query(Criteria.where("disable").is(true)), com.energy.model.project.Project.class);
        List<String> projectIds = authorities.stream().map(Authority::getProjectId).collect(Collectors.toList());
        List<String> disabledProjectIds = disabledProjects.stream().map(Project::getProjectId).collect(Collectors.toList());
        projectIds.removeAll(disabledProjectIds);

        if (projectIds.isEmpty()) {
            if (userId != 1)
                throw new IllegalArgumentException("没有授权给任何项目，不允许登陆");
            else
                //超级管理员登录，没有任何可用项目，切换到模板项目
                mongoTemplate.upsert(Query.query(Criteria.where("_id").is(token)), new Update().set("currentDataBase", "template"), TOKENS);
        } else {
            //默认切换到第一个项目
            mongoTemplate.upsert(Query.query(Criteria.where("_id").is(token)), new Update().set("currentDataBase", projectIds.get(0)), TOKENS);
            DataSourceHolder.setDB(projectIds.get(0));
            Employee employee = mongoTemplate.findOne(Query.query(Criteria.where("userId").is(userId.toString())), Employee.class);
            DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
            if (employee != null) {
                String userName = employee.getName() + "(" + employee.getUserName() + ")";
                mongoTemplate.upsert(Query.query(Criteria.where("_id").is(token)), new Update().set("userName", userName), TOKENS);
                eventService.sendLoginEvent(userName, userId.toString(), projectIds, OperateType.LOGIN);
            } else {
                //发布登录事件通知
                mongoTemplate.upsert(Query.query(Criteria.where("_id").is(token)), new Update().set("userName", authorities.get(0).getUserName()), TOKENS);
                eventService.sendLoginEvent(authorities.get(0).getUserName(), userId.toString(), projectIds, OperateType.LOGIN);
            }
        }
    }
}
