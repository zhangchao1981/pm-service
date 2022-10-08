package com.iscas.pm.api.service.impl.dev;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.dev.DevTaskMapper;
import com.iscas.pm.api.model.dev.DevRequirement;
import com.iscas.pm.api.model.dev.DevTask;
import com.iscas.pm.api.model.dev.RequireStatusEnum;
import com.iscas.pm.api.model.projectPlan.TaskFeedback;
import com.iscas.pm.api.model.projectPlan.TaskStatusEnum;
import com.iscas.pm.api.service.DevRequirementService;
import com.iscas.pm.api.service.DevTaskService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
import java.util.stream.Collectors;

/**
 * @author 66410
 * @description 针对表【dev_task(开发任务表)】的数据库操作Service实现
 * @createDate 2022-08-03 16:12:07
 */
@Service
public class DevTaskServiceImpl extends ServiceImpl<DevTaskMapper, DevTask> implements DevTaskService {
    @Autowired
    DevTaskMapper devTaskMapper;
    @Autowired
    DevRequirementService devRequirementService;

    @Override
    public Boolean addDevTask(DevTask devTask) {
        if (devTaskMapper.insert(devTask) < 1)
            return false;
        return true;
    }

    @Override
    public void computeDevTask(DevTask devTask, List<TaskFeedback> taskFeedbacks) {
        if (taskFeedbacks.size() > 0) {
            //计算任务的实际发生工时=所有反馈工时的和
            Double happenedHour = taskFeedbacks.stream().collect(Collectors.summingDouble(TaskFeedback::getWorkingHour));
            devTask.setHappenedHour(happenedHour);

            //计算任务实际开始时间=最早反馈日期
            devTask.setActualStartDate(taskFeedbacks.get(0).getDate());

            //计算任务进度=最晚反馈的进度
            Integer progress = taskFeedbacks.get(taskFeedbacks.size() - 1).getProgress();
            devTask.setDevProgress(progress);

            //任务完成
            if (devTask.getDevProgress() == 100) {
                //计算任务实际结束时间=最晚反馈日期
                devTask.setActualEndDate(taskFeedbacks.get(taskFeedbacks.size() - 1).getDate());

                //计算任务状态:计划结束日期>=实际结束时间 已完成；否则延迟完成
                if (devTask.getEndDate() == null || devTask.getEndDate().after(devTask.getActualEndDate())) {
                    devTask.setStatus(TaskStatusEnum.FINISHED);
                } else {
                    devTask.setStatus(TaskStatusEnum.DELAYED_FINISH);
                }
            }
            //任务未完成
            else {
                devTask.setActualEndDate(null);
                if (devTask.getEndDate() == null || new Date().before(devTask.getEndDate())) {
                    devTask.setStatus(TaskStatusEnum.RUNNING);
                } else {
                    devTask.setStatus(TaskStatusEnum.DELAYED);
                }
            }

            //更新计划任务
            devTaskMapper.updateById(devTask);

            //重新计算需求的已发生工时、进度、实际开始时间、实际结束时间、需求状态
            computeRequirementStatus(devTask.getRequireId());
            
           

        }
    }
    private void computeRequirementStatus(Integer requireId){
        //查询需求下所有任务
        List<DevTask> devTasks = super.list(new QueryWrapper<DevTask>().eq("require_id", requireId));

        if (devTasks.size() > 0) {
            //查询需求信息
            DevRequirement devRequirement = devRequirementService.getById(requireId);
            
            //计算需求的实际发生工时=所有任务发生工时的和
            Double happenedHour = devTasks.stream().mapToDouble(DevTask::getHappenedHour).sum();
            devRequirement.setHappenedHour(happenedHour);

            //计算需求实际开始时间=任务的最早实际开始时间
            Date actualStartDate = devTasks.stream().min(Comparator.comparing(DevTask::getActualStartDate)).get().getActualStartDate();
            devRequirement.setActualStartDate(actualStartDate);

            //计算需求进度=所有任务进度的平均值
            double progress = devTasks.stream().mapToInt(DevTask::getDevProgress).average().getAsDouble();
            devRequirement.setDevProgress(progress);

            //任务完成
            if (devRequirement.getDevProgress() == 100) {
                //计算任务实际结束时间=最晚反馈日期
                Date actualEndDate = devTasks.stream().max(Comparator.comparing(DevTask::getActualEndDate)).get().getActualEndDate();
                devRequirement.setActualEndDate(actualEndDate);

                //计算任务状态:计划结束日期>=实际结束时间 已完成；否则延迟完成
                if (devRequirement.getEndDate() == null || devRequirement.getEndDate().after(devRequirement.getActualEndDate())) {
                    devRequirement.setStatus(RequireStatusEnum.FINISHED);
                } else {
                    devRequirement.setStatus(RequireStatusEnum.DELAYED_FINISH);
                }
            }
            //任务未完成
            else {
                devRequirement.setActualEndDate(null);
                if (devRequirement.getEndDate() == null || new Date().before(devRequirement.getEndDate())) {
                    devRequirement.setStatus(RequireStatusEnum.DEVELOPING);
                } else {
                    devRequirement.setStatus(RequireStatusEnum.DELAYED);
                }
            }

            //更新计划任务
            devRequirementService.updateById(devRequirement);
        }

    }
}




