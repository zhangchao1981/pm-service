package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.iscas.pm.api.mapper.projectPlan.ProjectPlanMapper;
import com.iscas.pm.api.model.projectPlan.PlanTask;
import com.iscas.pm.api.model.projectPlan.TaskStatusEnum;
import com.iscas.pm.api.service.ProjectPlanService;
import com.iscas.pm.api.util.DateUtil;
import com.iscas.pm.common.core.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lichang
 * @description 针对表【plan】的数据库操作Service实现
 * @createDate 2022-07-28 17:13:09
 */
@Service
public class ProjectPlanServiceImpl extends ServiceImpl<ProjectPlanMapper, PlanTask> implements ProjectPlanService {
    @Autowired
    ProjectPlanMapper projectPlanMapper;


    //按wps转int的编号大小顺序查询
    @Override
    public List<PlanTask> getTaskListByWbs() {
        //拿到数据库查询结果，将其转成树结构
        //方案1. sort
        //方案2.重写compare方法
        List<PlanTask> planTasks = projectPlanMapper.selectList(new QueryWrapper<>());
        if (planTasks.size() < 1) {
            return null;
        }
        planTasks.sort((task1, task2) -> {
            String[] split = task1.getWbs().split("\\.");
            String[] split1 = task2.getWbs().split("\\.");

            int length = split.length > split1.length ? split1.length : split.length;
            for (int i = 0; i < length; i++) {
                if (split[i].equals(split1[i])) {
                    continue;
                }
                if (Integer.parseInt(split[i]) < Integer.parseInt(split1[i])) {
                    return -1;
                } else {
                    return 1;
                }
            }
            //编码有位数的部分全部相同
            return split.length > split1.length ? 1 : -1;
        });
        return planTasks;
    }


    @Override
    public List<PlanTask> getTaskList() {
        //拿到数据库查询结果，将其转成树结构
        List<PlanTask> planTasks = projectPlanMapper.selectList(new QueryWrapper<PlanTask>().orderByAsc("parent_id", "position"));
        if (planTasks.size() < 1) {
            return null;
        }
        List<PlanTask> planTaskTree = TreeUtil.treeOut(planTasks, PlanTask::getId, PlanTask::getParentId, PlanTask::getChildren);

        return planTaskTree;
    }

    @Override
    public PlanTask addTask(PlanTask planTask) {
        Integer position = planTask.getPosition();
        Integer parentId = planTask.getParentId();
        //查询父任务信息
        final PlanTask parent = projectPlanMapper.selectById(planTask.getParentId());
        if (parent == null && parentId != 0)
            throw new IllegalArgumentException("父任务不存在");

        //新任务之后的任务后移
        List<PlanTask> updatedPlanTasks = movePosition(planTask.getId(), parentId, position, null, false);
        if (updatedPlanTasks.size() > 0)
            super.updateBatchById(updatedPlanTasks);

        //插入新任务到数据库
        planTask.setWorkingDays(DateUtil.daysBetween(planTask.getStartDate(), planTask.getEndDate()));
        planTask.setWbs(parent == null ? position.toString() : parent.getWbs() + "." + position);
        planTask.setPersonCount(planTask.getWorkerList() == null ? 0 : planTask.getWorkerList().size());
        planTask.setStatus(getStatus(planTask.getStartDate(), planTask.getEndDate()));
        projectPlanMapper.insert(planTask);

        return planTask;
    }

    @Override
    public Boolean editTask(PlanTask planTask) {
        PlanTask db_task = projectPlanMapper.selectById(planTask.getId());
        if (db_task == null)
            throw new IllegalArgumentException("修改的任务不存在");

        PlanTask parent = null;
        if (planTask.getParentId() != 0) {
            parent = projectPlanMapper.selectById(planTask.getParentId());
            if (parent == null)
                throw new IllegalArgumentException("父任务不存在");
        }

        List<PlanTask> updatedPlanTasks = new ArrayList<>();

        //父任务未发生变化
        if (planTask.getParentId().equals(db_task.getParentId())) {
            //排序位置前移
            if (planTask.getPosition() < db_task.getPosition()) {
                updatedPlanTasks.addAll(movePosition(planTask.getId(), planTask.getParentId(), planTask.getPosition(), db_task.getPosition(), false));
            }
            //排序位置后移
            else if (planTask.getPosition() > db_task.getPosition()) {
                //由于前端参数问题，需要做-1处理
                planTask.setPosition(planTask.getPosition() - 1);
                updatedPlanTasks.addAll(movePosition(planTask.getId(), planTask.getParentId(), db_task.getPosition(), planTask.getPosition(), true));
            }
        }
        //父任务发生变化
        else {
            //原父任务下子任务前移
            updatedPlanTasks.addAll(movePosition(planTask.getId(), db_task.getParentId(), db_task.getPosition(), null, true));
            //新父任务下子任务后移
            updatedPlanTasks.addAll(movePosition(planTask.getId(), planTask.getParentId(), planTask.getPosition(), null, false));
        }

        //判断新的父任务是否在变化列表中
        if (parent != null) {
            Map<Integer, PlanTask> updatePlanTaskMap = updatedPlanTasks.stream().collect(Collectors.toMap(PlanTask::getId, Function.identity(), (key1, key2) -> key1));
            if (updatePlanTaskMap.containsKey(parent.getId()))
                parent = updatePlanTaskMap.get(parent.getId());
        }

        //更新当前移动的任务
        planTask.setPersonCount(planTask.getWorkerList() == null ? 0 : planTask.getWorkerList().size());
        planTask.setStatus(getStatus(planTask.getStartDate(), planTask.getEndDate()));
        planTask.setWorkingDays(DateUtil.daysBetween(planTask.getStartDate(), planTask.getEndDate()));
        planTask.setWbs(parent == null ? planTask.getPosition().toString() : parent.getWbs() + "." + planTask.getPosition());
        updatedPlanTasks.add(planTask);

        //更新移动的任务下的所有子任务的wbs
        if (!db_task.getWbs().equals(planTask.getWbs())) {
            QueryWrapper<PlanTask> queryWrapper = new QueryWrapper<>();
            queryWrapper.likeRight("wbs", db_task.getWbs()).ne("wbs", db_task.getWbs());
            List<PlanTask> childPlanTasks = projectPlanMapper.selectList(queryWrapper);

            childPlanTasks.forEach(childTask -> {
                childTask.setWbs(childTask.getWbs().replaceFirst(db_task.getWbs(), planTask.getWbs()));
            });
            updatedPlanTasks.addAll(childPlanTasks);
        }

        if (updatedPlanTasks.size() > 0)
            super.updateBatchById(updatedPlanTasks);

        return true;
    }

    @Override
    public Boolean deleteTask(Integer id) {
        PlanTask task = projectPlanMapper.selectById(id);
        if (task == null)
            throw new IllegalArgumentException("待删除任务不存在");

        //有子任务的任务不允许删除
        QueryWrapper<PlanTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parent_id", id);
        List<PlanTask> subTasks = projectPlanMapper.selectList(queryWrapper);
        if (subTasks.size() > 0)
            throw new IllegalArgumentException("该任务下存在子任务，请先删除子任务");

        //待删除任务以后的任务前移
        List<PlanTask> updatedPlanTasks = movePosition(id, task.getParentId(), task.getPosition(), null, true);
        if (updatedPlanTasks.size() > 0)
            super.updateBatchById(updatedPlanTasks);

        //删除任务
        projectPlanMapper.deleteById(id);

        return true;
    }

    /**
     * 移动排序位置
     *
     * @param parentId      父任务id
     * @param startPosition 受影响记录的起始位置
     * @param endPosition   受影响记录的结束位置
     * @param isForward     是否是前移
     */
    private List<PlanTask> movePosition(Integer id, Integer parentId, Integer startPosition, Integer endPosition, boolean isForward) {
        List<PlanTask> updatedPlanTasks = new ArrayList<>();

        //查询出受排序影响的任务列表
        QueryWrapper<PlanTask> queryWrapper = new QueryWrapper<PlanTask>()
                .eq("parent_id", parentId)
                .ge(startPosition != null, "position", startPosition)
                .le(endPosition != null, "position", endPosition)
                .ne(id != null,"id", id);
        List<PlanTask> planTasks = projectPlanMapper.selectList(queryWrapper);

        //查询受影响的任务列表的wbs和排序编号
        planTasks.forEach(plan -> {
            //查询出所有子任务
            String oldWbs = plan.getWbs();
            QueryWrapper<PlanTask> childQueryWrapper = new QueryWrapper<>();
            childQueryWrapper.likeRight("wbs", oldWbs).ne("wbs", oldWbs);
            List<PlanTask> childPlanTasks = projectPlanMapper.selectList(childQueryWrapper);

            plan.setPosition(isForward ? plan.getPosition() - 1 : plan.getPosition() + 1);
            if (plan.getWbs().lastIndexOf(".") == -1)
                plan.setWbs(plan.getPosition().toString());
            else {
                String wbs = plan.getWbs().substring(0, plan.getWbs().lastIndexOf("."));
                plan.setWbs(wbs + "." + plan.getPosition().toString());
            }

            //替换所有子任务的wbs
            childPlanTasks.forEach(planTask -> {
                planTask.setWbs(planTask.getWbs().replaceFirst(oldWbs, plan.getWbs()));
            });

            updatedPlanTasks.add(plan);
            updatedPlanTasks.addAll(childPlanTasks);

        });
        return updatedPlanTasks;
    }

    private TaskStatusEnum getStatus(Date start, Date end) {
        if (new Date().before(start))
            return TaskStatusEnum.UN_START;
        else if (start.before(new Date()) && new Date().before(end))
            return TaskStatusEnum.RUNNING;
        else
            return TaskStatusEnum.DELAYED;
    }

}




