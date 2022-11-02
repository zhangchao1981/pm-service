package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.dev.DevTaskMapper;
import com.iscas.pm.api.mapper.projectPlan.ProjectPlanMapper;
import com.iscas.pm.api.mapper.projectPlan.TaskFeedbackMapper;
import com.iscas.pm.api.model.dev.DevTask;
import com.iscas.pm.api.model.projectPlan.PlanTask;
import com.iscas.pm.api.model.projectPlan.TaskFeedback;
import com.iscas.pm.api.service.DevTaskService;
import com.iscas.pm.api.service.ProjectPlanService;
import com.iscas.pm.api.service.TaskFeedbackService;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * @author zhangchao
 * @description 针对表【task_feedback(任务反馈表)】的数据库操作Service实现
 * @createDate 2022-08-02 09:37:30
 */
@Service
public class TaskFeedbackServiceImpl extends ServiceImpl<TaskFeedbackMapper, TaskFeedback> implements TaskFeedbackService {
    @Autowired
    TaskFeedbackMapper taskFeedbackMapper;
    @Autowired
    ProjectPlanMapper projectPlanMapper;
    @Autowired
    DevTaskMapper devTaskMapper;
    @Autowired
    ProjectPlanService projectPlanService;
    @Autowired
    DevTaskService devTaskService;

    @Override
    public List<TaskFeedback> selectListByTaskId(TaskFeedback taskFeedback) {
        QueryWrapper<TaskFeedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(taskFeedback.getPlanTaskId() != null, "plan_task_id", taskFeedback.getPlanTaskId());
        queryWrapper.eq(taskFeedback.getDevTaskId() != null, "dev_task_id", taskFeedback.getDevTaskId());
        queryWrapper.eq(taskFeedback.getUserId() != null, "user_id", taskFeedback.getUserId());
        queryWrapper.orderByAsc("date");
        return taskFeedbackMapper.selectList(queryWrapper);
    }

    @Override
    public void saveTaskFeedback(TaskFeedback taskFeedback) {
        if (taskFeedback.getPlanTaskId() != null)
            savePlanTaskFeedback(taskFeedback);
        else if (taskFeedback.getDevTaskId() != null)
            saveDevTaskFeedback(taskFeedback);
    }

    private void savePlanTaskFeedback(TaskFeedback taskFeedback) {
        //查询反馈对应的任务
        PlanTask planTask = projectPlanMapper.selectById(taskFeedback.getPlanTaskId());
        if (planTask == null) {
            throw new IllegalArgumentException("反馈的任务不存在");
        }

        //校验当前用户是否有权填写反馈
        Integer userId = RequestHolder.getUserInfo().getId();
        if (planTask.getWorkerIds() == null || !planTask.getWorkerIds().contains(userId)) {
            throw new IllegalArgumentException("该任务没有指定给您，您不需要填写反馈");
        }

        //查询该任务下的当前用户的所有反馈
        List<TaskFeedback> my_feedbacks = selectListByTaskId(new TaskFeedback().setPlanTaskId(planTask.getId()));
        //校验填写进度是否在合法区间
        checkProgress(taskFeedback, my_feedbacks);

        //数据库中新增或修改反馈
        taskFeedback.setUserId(userId);
        taskFeedback.setPersonName(RequestHolder.getUserInfo().getEmployeeName());
        taskFeedback.setCreateTime(new Date());
        super.saveOrUpdate(taskFeedback);

        //计算任务的已发生工时、任务进度、任务实际开始时间、任务结束时间、任务状态
        List<TaskFeedback> all_feedbacks = selectListByTaskId(new TaskFeedback().setPlanTaskId(planTask.getId()));
        projectPlanService.computePlanTask(planTask, all_feedbacks);
    }

    private void saveDevTaskFeedback(TaskFeedback taskFeedback) {
        //查询开发任务
        DevTask devTask = devTaskMapper.selectById(taskFeedback.getDevTaskId());
        if (devTask == null)
            throw new IllegalArgumentException("反馈的开发任务不存在");

        //校验当前用户是否有权填写反馈
        Integer userId = RequestHolder.getUserInfo().getId();
        if (!userId.equals(devTask.getWorkerId())) {
            throw new IllegalArgumentException("该任务没有指定给您，您不需要填写反馈");
        }

        //查询该任务下的所有反馈
        List<TaskFeedback> my_feedbacks = selectListByTaskId(new TaskFeedback().setDevTaskId(devTask.getId()).setUserId(userId));
        //校验填写进度是否在合法区间
        checkProgress(taskFeedback, my_feedbacks);

        //数据库中新增或修改反馈
        taskFeedback.setUserId(userId);
        taskFeedback.setPersonName(RequestHolder.getUserInfo().getEmployeeName());
        taskFeedback.setCreateTime(new Date());
        super.saveOrUpdate(taskFeedback);

        //计算任务的已发生工时、任务进度、任务实际开始时间、任务结束时间、任务状态
        List<TaskFeedback> all_feedbacks = selectListByTaskId(new TaskFeedback().setDevTaskId(devTask.getId()));
        devTaskService.computeDevTask(devTask, all_feedbacks);

    }

    private void checkProgress(TaskFeedback taskFeedback, List<TaskFeedback> my_feedbacks) {
        //计算本次反馈可填写的进度区间
        Integer progress_min = 0;
        Integer progress_max = 100;
        for (TaskFeedback feedback : my_feedbacks) {
            if (taskFeedback.getDate().after(feedback.getDate())) {
                progress_min = feedback.getProgress();
            } else if (taskFeedback.getDate().getTime() == feedback.getDate().getTime()) {
                taskFeedback.setId(feedback.getId());
            } else {
                progress_max = feedback.getProgress();
                break;
            }
        }

        //校验进度是否在合法区间
        if (taskFeedback.getProgress() < progress_min || taskFeedback.getProgress() > progress_max)
            throw new IllegalArgumentException("反馈进度只能按日期递增，当前可填写的进度范围为:" + progress_min + "-" + progress_max);
    }

}




