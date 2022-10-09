package com.iscas.pm.api.service.impl.dev;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.dev.DevRequirementMapper;
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

import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

/**
 * @author 66410
 * @description 针对表【dev_requirement(开发需求表)】的数据库操作Service实现
 * @createDate 2022-08-03 11:21:48
 */
@Service
public class DevRequirementServiceImpl extends ServiceImpl<DevRequirementMapper, DevRequirement> implements DevRequirementService {
    @Autowired
    DevTaskService devTaskService;

    @Override
    public void computeRequirementStatus(Integer requireId) {
        //查询需求下所有任务
        List<DevTask> devTasks = devTaskService.list(new QueryWrapper<DevTask>().eq("require_id", requireId));

        if (devTasks.size() > 0) {
            //查询需求信息
            DevRequirement devRequirement = super.getById(requireId);

            //计算需求的实际发生工时=所有任务发生工时的和
            Double happenedHour = devTasks.stream().mapToDouble(DevTask::getHappenedHour).sum();
            devRequirement.setHappenedHour(happenedHour);

            //计算需求实际开始时间=任务的最早实际开始时间
            Optional<DevTask> min = devTasks.stream().filter(devTask -> devTask.getActualStartDate() != null).min(Comparator.comparing(DevTask::getActualStartDate));
            Date actualStartDate = min.isPresent() ? min.get().getActualStartDate() : null;
            devRequirement.setActualStartDate(actualStartDate);

            //计算需求进度=所有任务进度的平均值
            double progress = devTasks.stream().mapToInt(DevTask::getDevProgress).average().getAsDouble();
            devRequirement.setDevProgress(progress);

            //任务完成
            if (devRequirement.getDevProgress() == 100) {
                //计算任务实际结束时间=最晚反馈日期
                Optional<DevTask> max = devTasks.stream().filter(devTask -> devTask.getActualStartDate() != null).max(Comparator.comparing(DevTask::getActualEndDate));
                Date actualEndDate = max.isPresent()  ? max.get().getActualEndDate() : null;
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
            super.updateById(devRequirement);
        }

    }
}




