package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.dev.DevTaskMapper;
import com.iscas.pm.api.mapper.projectPlan.ProjectPlanMapper;
import com.iscas.pm.api.model.dev.DevTask;
import com.iscas.pm.api.model.projectPlan.PlanTask;
import com.iscas.pm.api.model.projectPlan.TaskFeedback;
import com.iscas.pm.api.model.projectPlan.TaskStatusEnum;
import com.iscas.pm.api.service.TaskFeedbackService;
import com.iscas.pm.api.mapper.projectPlan.TaskFeedbackMapper;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

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

    public List<TaskFeedback> selectListByPlanTaskId(TaskFeedback taskFeedback) {
        QueryWrapper<TaskFeedback> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq(taskFeedback.getPlanTaskId() != null, "plan_task_id", taskFeedback.getPlanTaskId());
        queryWrapper.eq(taskFeedback.getDevTaskId() != null, "dev_task_id", taskFeedback.getDevTaskId());
        return taskFeedbackMapper.selectList(queryWrapper);
    }

    @Override
    public void saveTaskFeedback(TaskFeedback taskFeedback) {
       if (taskFeedback.getPlanTaskId() != null)
           savePlanTaskFeedback(taskFeedback);
        else if (taskFeedback.getDevTaskId() != null)
           saveDevTaskFeedback(taskFeedback);
    }

    private void savePlanTaskFeedback(TaskFeedback taskFeedback){
        //查询计划任务
        PlanTask planTask = projectPlanMapper.selectById(taskFeedback.getPlanTaskId());
        if (planTask == null) {
            throw new IllegalArgumentException("反馈的任务不存在");
        }

        //补全信息，新增或更新反馈
        Integer userId = RequestHolder.getUserInfo().getId();
        taskFeedback.setUserId(userId);
        taskFeedback.setPersonName(RequestHolder.getUserInfo().getEmployeeName());
        taskFeedback.setCreateTime(new Date());
        QueryWrapper<TaskFeedback> wrapper = new QueryWrapper<TaskFeedback>()
                .eq("user_id", userId)
                .eq("date", taskFeedback.getDate())
                .eq("plan_task_id", taskFeedback.getPlanTaskId());
        TaskFeedback feedback = taskFeedbackMapper.selectOne(wrapper);
        if (feedback != null)
            taskFeedback.setId(feedback.getId());
        super.saveOrUpdate(taskFeedback);

        //查询该计划任务的所有反馈
        List<TaskFeedback> taskFeedbacks = selectListByPlanTaskId(new TaskFeedback().setPlanTaskId(taskFeedback.getPlanTaskId()));

        //计算任务的实际发生工时=所有反馈工时的和
        Double happenedHour = taskFeedbacks.stream().collect(Collectors.summingDouble(TaskFeedback::getWorkingHour));
        planTask.setHappenedHour(happenedHour);

        //计算任务进度=所有人进度最大值求和/人数
        if (planTask.getPersonCount() > 0) {
            Integer progress_total = taskFeedbacks.stream().collect(Collectors.groupingBy(TaskFeedback::getUserId, Collectors.summarizingInt(TaskFeedback::getProgress)))
                    .entrySet().stream().mapToInt(t -> t.getValue().getMax()).sum();
            Integer progress = progress_total / planTask.getPersonCount();
            planTask.setProgressRate(progress);
        }

        //计算任务实际开始时间=最早反馈日期
        LongSummaryStatistics statistics = taskFeedbacks.stream().collect(Collectors.summarizingLong(item -> item.getDate().getTime()));
        if (statistics.getMin() != 0)
            planTask.setActualStartDate(new Date(statistics.getMin()));


        if (planTask.getProgressRate() == 100) {
            //计算任务实际结束时间=最晚反馈日期
            if (statistics.getMax() != 0) {
                planTask.setActualEndDate(new Date(statistics.getMax()));
            }

            //计算任务状态:计划结束日期>=实际结束时间 已完成；否则延迟完成
            if (planTask.getStartDate().after(new Date(statistics.getMax()))) {
                planTask.setStatus(TaskStatusEnum.FINISHED);
            } else {
                planTask.setStatus(TaskStatusEnum.DELAYED_FINISH);
            }
        }

        //更新计划任务
        projectPlanMapper.updateById(planTask);
    }

    private void saveDevTaskFeedback(TaskFeedback taskFeedback){
        //查询开发任务
        DevTask devTask = devTaskMapper.selectById(taskFeedback.getDevTaskId());
        if (devTask == null)
            throw new IllegalArgumentException("反馈的任务不存在");

        //补全信息，新增或更新反馈
        Integer userId = RequestHolder.getUserInfo().getId();
        taskFeedback.setUserId(userId);
        taskFeedback.setPersonName(RequestHolder.getUserInfo().getEmployeeName());
        taskFeedback.setCreateTime(new Date());
        QueryWrapper<TaskFeedback> wrapper = new QueryWrapper<TaskFeedback>()
                .eq("user_id", userId)
                .eq("date", taskFeedback.getDate())
                .eq("dev_task_id", taskFeedback.getDevTaskId());
        TaskFeedback feedback = taskFeedbackMapper.selectOne(wrapper);
        if (feedback != null)
            taskFeedback.setId(feedback.getId());
        super.saveOrUpdate(taskFeedback);

        //查询该开发任务的所有反馈
        List<TaskFeedback> taskFeedbacks = selectListByPlanTaskId(new TaskFeedback().setPlanTaskId(taskFeedback.getDevTaskId()));

        //计算任务的实际发生工时=所有反馈工时的和
        Double happenedHour = taskFeedbacks.stream().collect(Collectors.summingDouble(TaskFeedback::getWorkingHour));
        devTask.setHappenedHour(happenedHour);

        //计算任务进度=所有人进度最大值求和/人数
        if (devTask.getWorker().split(",").length > 0) {
            Integer progress_total = taskFeedbacks.stream().collect(Collectors.groupingBy(TaskFeedback::getUserId, Collectors.summarizingInt(TaskFeedback::getProgress)))
                    .entrySet().stream().mapToInt(t -> t.getValue().getMax()).sum();
            Integer progress = progress_total / devTask.getWorker().split(",").length;
            devTask.setDevProgress(progress);
        }

        //计算任务实际开始时间=最早反馈日期
        LongSummaryStatistics statistics = taskFeedbacks.stream().collect(Collectors.summarizingLong(item -> item.getDate().getTime()));

        if (devTask.getDevProgress() == 100) {
            //计算任务状态:计划结束日期>=实际结束时间 已完成；否则延迟完成
            if (devTask.getStartDate().after(new Date(statistics.getMax()))) {
                devTask.setStatus(TaskStatusEnum.FINISHED);
            } else {
                devTask.setStatus(TaskStatusEnum.DELAYED_FINISH);
            }
        }

        //更新计划任务
        devTaskMapper.updateById(devTask);
    }

}




