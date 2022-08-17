package com.iscas.pm.api.timer;

import com.iscas.pm.api.model.doc.DocTemplate;
import com.iscas.pm.api.model.doc.Document;
import com.iscas.pm.api.service.DocTemplateService;
import com.iscas.pm.api.service.DocumentService;
import com.iscas.pm.common.core.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

/**
 * 定时任务 spring task
 * @author by  lichang
 * @date 2022/8/17.
 */
@Component
public class ClearInvalidFile {
    @Autowired
    DocTemplateService docTemplateService;
    @Autowired
    DocumentService documentService;
    @Autowired
    RedisUtil redisUtil;


    //反复被执行的方法 隔5秒钟执行一次(测试)
    @Scheduled(cron = "0/5 * * * * ?")
    public void loadGoodsPushRedis() {
        //查询数据库全部路径，有效的路径存储时间延期  无效的略过
        List<String> documentPaths = documentService.list().stream().map(Document::getPath).collect(Collectors.toList());
        List<String> templatePaths = docTemplateService.list().stream().map(DocTemplate::getPath).collect(Collectors.toList());
        documentPaths.stream().forEach(documentPath-> {if (redisUtil.hasKey(documentPath)){ redisUtil.expire(documentPath,5000); }});
        templatePaths.stream().forEach(templatePath-> {if (redisUtil.hasKey(templatePath)){ redisUtil.expire(templatePath,5000); }});
    }


}
