package com.iscas.pm.api.service;


import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.dev.DevTask;

/**
* @author 66410
* @description 针对表【dev_task(开发任务表)】的数据库操作Service
* @createDate 2022-08-03 16:12:07
*/
public interface DevTaskService extends IService<DevTask> {

    Boolean addDevTask(DevTask devTask);
}
