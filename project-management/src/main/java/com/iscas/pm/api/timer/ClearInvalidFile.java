package com.iscas.pm.api.timer;

import com.iscas.pm.api.model.doc.DocTemplate;
import com.iscas.pm.api.model.doc.Document;
import com.iscas.pm.api.service.DocTemplateService;
import com.iscas.pm.api.service.DocumentService;
import com.iscas.pm.common.core.util.RedisUtil;
import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
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

    //每次执行都是创建一个新的会话(而之前设置的，新建会话会切换数据库到default，需要固定该任务是在项目库里执行的(setDB)
//    CronTrigger配置完整格式为： [秒][分] [小时][日] [月][周] [年]
    @Scheduled(cron = "0 0 0/1 * * ?")
    //每小时触发一次
    public void loadGoodsPushRedis() {
        //暂定切换到 静态项目
        DataSourceHolder.setDB("project_demo");
        //查询数据库全部路径，有效的路径存储时间延期  无效的略过
        List<String> documentPaths = documentService.list().stream().map(Document::getPath).collect(Collectors.toList());
        // template更换到主库了
//        List<String> templatePaths = docTemplateService.list().stream().map(DocTemplate::getPath).collect(Collectors.toList());
        documentPaths.stream().forEach(documentPath-> {if (redisUtil.hasKey(documentPath)){ redisUtil.expire(documentPath,5000); }});
//        templatePaths.stream().forEach(templatePath-> {if (redisUtil.hasKey(templatePath)){ redisUtil.expire(templatePath,5000); }});
    }
}
