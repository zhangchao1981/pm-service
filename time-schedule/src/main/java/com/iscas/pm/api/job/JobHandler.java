package com.iscas.pm.api.job;

import com.xxl.job.core.handler.IJobHandler;
import com.xxl.job.core.handler.annotation.XxlJob;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;

import java.io.Serializable;

/**
 * @author by  lichang
 * @date 2022/10/14.
 */

@Component
@Slf4j
public class JobHandler extends IJobHandler implements Serializable {
//  @Autowired
//  DocTemplateService docTemplateService;
    //每小时触发一次
    @XxlJob("TestJobHandler")
    public void demoJobHandler() throws Exception {
        //暂定切换到 静态项目
//        DataSourceHolder.setDB("project_demo");
//        //查询数据库全部路径，有效的路径存储时间延期  无效的略过
//        System.out.println("XXL任务执行成功");
//        docTemplateService.list().forEach(System.out::println);
//        XxlJobHelper.log("XXL-JOB, Hello World.");
    }
    @Override
    public void execute() throws Exception {
    }

}
