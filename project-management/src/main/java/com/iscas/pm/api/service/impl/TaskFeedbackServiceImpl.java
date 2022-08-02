package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.model.projectPlan.PlanTask;
import com.iscas.pm.api.model.projectPlan.TaskFeedback;
import com.iscas.pm.api.service.TaskFeedbackService;
import com.iscas.pm.api.mapper.projectPlan.TaskFeedbackMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
* @author zhangchao
* @description 针对表【task_feedback(任务反馈表)】的数据库操作Service实现
* @createDate 2022-08-02 09:37:30
*/
@Service
public class TaskFeedbackServiceImpl extends ServiceImpl<TaskFeedbackMapper, TaskFeedback> implements TaskFeedbackService{
    @Autowired
    TaskFeedbackMapper taskFeedbackMapper;

    public List<TaskFeedback> selectListByPlanTaskId(Integer planTaskId){
        QueryWrapper<TaskFeedback> queryWrapper = new QueryWrapper<TaskFeedback>()
                .eq("plan_task_id", planTaskId);
        return taskFeedbackMapper.selectList(queryWrapper);
    }

}




