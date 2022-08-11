package com.iscas.pm.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.projectPlan.PlanTask;

import java.util.List;

/**
* @author 66410
* @description 针对表【plan】的数据库操作Service
* @createDate 2022-07-28 17:13:09
*/
public interface ProjectPlanService extends IService<PlanTask> {

    List<PlanTask> getTaskList();

    PlanTask addTask(PlanTask planTask);

    Boolean editTask(PlanTask planTask);

    Boolean deleteTask(Integer id);
}
