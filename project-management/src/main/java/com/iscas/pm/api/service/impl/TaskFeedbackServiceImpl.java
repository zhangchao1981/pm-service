package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.projectPlan.ProjectPlanMapper;
import com.iscas.pm.api.model.projectPlan.PlanTask;
import com.iscas.pm.api.model.projectPlan.TaskFeedback;
import com.iscas.pm.api.model.projectPlan.TaskStatusEnum;
import com.iscas.pm.api.service.TaskFeedbackService;
import com.iscas.pm.api.mapper.projectPlan.TaskFeedbackMapper;
import com.iscas.pm.common.core.web.filter.RequestHolder;
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

    public List<TaskFeedback> selectListByPlanTaskId(Integer planTaskId) {
        return taskFeedbackMapper.selectList(new QueryWrapper<TaskFeedback>().eq("plan_task_id", planTaskId));
    }

    @Override
    public void saveTaskFeedback(TaskFeedback taskFeedback) {
        //查询计划任务
        PlanTask planTask = projectPlanMapper.selectById(taskFeedback.getPlanTaskId());
        if (planTask == null)
            throw new IllegalArgumentException("反馈的任务不存在");

        //补全信息后，存入反馈表
        taskFeedback.setUserId(RequestHolder.getUserInfo().getId());
        taskFeedback.setPersonName(RequestHolder.getUserInfo().getEmployeeName());
        taskFeedback.setCreateTime(new Date());
        super.saveOrUpdate(taskFeedback);

        //查询该计划任务的所有反馈
        List<TaskFeedback> taskFeedbacks = selectListByPlanTaskId(taskFeedback.getPlanTaskId());

        //计算实际发生工时=所有反馈工时的和
        Double happenedHour = taskFeedbacks.stream().collect(Collectors.summingDouble(TaskFeedback::getWorkingHour));
        planTask.setHappenedHour(happenedHour);

        //计算进度=所有人进度最大值求和/人数
        if (planTask.getPersonCount() > 0) {
            Integer progress_total = taskFeedbacks.stream().collect(Collectors.groupingBy(TaskFeedback::getUserId, Collectors.summarizingInt(TaskFeedback::getProgress)))
                    .entrySet().stream().mapToInt(t -> t.getValue().getMax()).sum();
            Integer progress = progress_total / planTask.getPersonCount();
            planTask.setProgressRate(progress);
        }

        //计算实际开始时间=最早反馈日期
        LongSummaryStatistics statistics = taskFeedbacks.stream().collect(Collectors.summarizingLong(item -> item.getDate().getTime()));
        if (statistics.getMin() != 0)
            planTask.setActualStartDate(new Date(statistics.getMin()));


        if (planTask.getProgressRate() == 100) {
            //计算实际结束时间=最晚反馈日期
            if (statistics.getMax() != 0) {
                planTask.setActualStartDate(new Date(statistics.getMax()));
            }

            //计算状态:计划结束日期>=实际结束时间 已完成；否则延迟完成
            if (planTask.getStartDate().after(new Date(statistics.getMax()))) {
                planTask.setStatus(TaskStatusEnum.RUNNING);
            } else {
                planTask.setStatus(TaskStatusEnum.DELAYED_FINISH);
            }
        }

        //更新计划任务
        projectPlanMapper.updateById(planTask);

    }

}




