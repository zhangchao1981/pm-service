package com.iscas.pm.api.util;


import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.util.LinkedMultiValueMap;
import org.springframework.util.MultiValueMap;
import org.springframework.web.client.RestTemplate;

import java.util.Objects;

@Slf4j
public class XxlJobUtil {

    public static String domain = "http://time-schedule-admin:18082/xxl-job-admin";
    public static final HttpHeaders headers = new HttpHeaders();
    public static Long cookieFailure;

    public static HttpHeaders getToken() {
        if (headers.isEmpty()) {
            return doLogin();
        }

        //不知道token有效期，暂定1小时更新一次
        if (System.currentTimeMillis() - cookieFailure > 3600000) {
            return doLogin();
        }
        return headers;
    }

    private static HttpHeaders refreshToken(){
        return doLogin();
    }

    public static HttpHeaders doLogin() {
        String url = domain + "/login";

        MultiValueMap map = new LinkedMultiValueMap();
        map.add("userName", "admin");
        map.add("password", "R248dllFX=X,ff1");

        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, map, String.class);

        HttpHeaders header = responseEntity.getHeaders();
        String token = Objects.requireNonNull(header.get("Set-Cookie")).get(0).split(";")[0].replace("XXL_JOB_LOGIN_IDENTITY=", "");
        headers.clear();
        headers.add("cookie", "XXL_JOB_LOGIN_IDENTITY=" + token);
        headers.add("Content-Type", "application/x-www-form-urlencoded; charset=UTF-8");
        cookieFailure = System.currentTimeMillis();
        //log.info("==========重新调用login接口获取token");
        return headers;
    }

    private static ResponseEntity<String> postEntity(String url, MultiValueMap request){
        HttpHeaders headers = getToken();
        HttpEntity<MultiValueMap<String, Object>> r = new HttpEntity<>(request, headers);
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<String> responseEntity = restTemplate.postForEntity(url, r, String.class);
        if (!Objects.equals(HttpStatus.OK, responseEntity.getStatusCode())) {
            log.error("xxl-job调用失败,开始重试");
            headers = refreshToken();
            r = new HttpEntity<>(request, headers);
            responseEntity = restTemplate.postForEntity(url, r, String.class);
            if (!Objects.equals(HttpStatus.OK, responseEntity.getStatusCode())){
                log.error("刷新token后依旧调用失败，请联系管理员");
                return responseEntity;
            }else {
                log.info("新token有效了，goodJob");
                return responseEntity;
            }
        }
        return responseEntity;
    }

    public static MultiValueMap<String, Object> getCreateRawBody() {
        MultiValueMap body = new LinkedMultiValueMap();
        body.add("alarmEmail", "");
        body.add("childJobId", "");
        body.add("executorBlockStrategy", "SERIAL_EXECUTION");
        body.add("executorFailRetryCount", 0);
        body.add("executorHandler", "jobHandler");
        body.add("executorRouteStrategy", "FIRST");
        body.add("executorTimeout", 0);
        body.add("glueRemark", "GLUE代码初始化");
        body.add("glueSource", "");
        body.add("glueType", "BEAN");
        body.add("jobGroup", 1);
        body.add("misfireStrategy", "DO_NOTHING");
        body.add("scheduleType", "CRON");
        body.add("schedule_conf_FIX_DELAY", "");
        body.add("schedule_conf_FIX_RATE", "");
        return body;
    }

    public static String createJob(String name, String param, String cron) {
        String url = domain + "/jobinfo/add";

        MultiValueMap<String, Object> map = getCreateRawBody();
        map.add("schedule_conf_CRON", cron);
        map.add("scheduleConf", cron);
        map.add("author", "xxl");
        map.add("cronGen_display", cron);
        map.add("executorParam", param);
        map.add("jobDesc", name);

        ResponseEntity<String> responseEntity = postEntity(url,map);

        if (!Objects.equals(HttpStatus.OK, responseEntity.getStatusCode())) {
            throw new IllegalArgumentException("创建定时任务【" + name + "】失败");
        }

        JSONObject jsonObject = JSON.parseObject(responseEntity.getBody());
        String id = jsonObject.get("content").toString();

        startJob(name, id);
        return id;
    }

    public static String updateJob(String id, String name, String param, String description, String cron) {
        String url = domain + "/jobinfo/update";

        MultiValueMap<String, Object> map = getCreateRawBody();
        map.add("schedule_conf_CRON", cron);
        map.add("scheduleConf", cron);
        map.add("author", "xxl");
        map.add("cronGen_display", cron);
        map.add("executorParam", param);
        map.add("jobDesc", description);
        map.add("id", id);

        ResponseEntity<String> responseEntity = postEntity(url,map);

        if (!Objects.equals(HttpStatus.OK, responseEntity.getStatusCode())) {
            throw new IllegalArgumentException("更新定时任务【" + name + "】失败");
        }

        startJob(name, id);
        return id;
    }

    public static void removeJob(String id) {
        try {
            String url = domain + "/jobinfo/remove";

            MultiValueMap<String, Object> map = new LinkedMultiValueMap();
            map.add("id", id);

            ResponseEntity<String> responseEntity = postEntity(url,map);

            if (Objects.equals(HttpStatus.OK, responseEntity.getStatusCode())) {
                log.info("移除定时任务【{}】成功", id);
            } else {
                throw new IllegalArgumentException("移除定时任务【" + id + "】失败");
            }
        } catch (Exception e) {
            log.error("移除定时任务【" + id + "】失败", e);
        }
    }

    public static void startJob(String name, String id) {
        String url = domain + "/jobinfo/start";

        MultiValueMap<String, Object> map = new LinkedMultiValueMap();
        map.add("id", id);

        ResponseEntity<String> responseEntity = postEntity(url,map);

        if (!Objects.equals(HttpStatus.OK, responseEntity.getStatusCode())) {
            throw new IllegalArgumentException("启动定时任务【" + name + "-" + id + "】失败");
        }
    }

    public static void stopJob(String name, String id) {
        String url = domain + "/jobinfo/stop";

        MultiValueMap<String, Object> map = new LinkedMultiValueMap();
        map.add("id", id);

        ResponseEntity<String> responseEntity = postEntity(url,map);

        if (!Objects.equals(HttpStatus.OK, responseEntity.getStatusCode())) {
            throw new IllegalArgumentException("停止定时任务【" + name + "-" + id + "】失败");
        }
    }
}
