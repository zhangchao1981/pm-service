package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.iscas.pm.api.mapper.projectPlan.ProjectPlanMapper;
import com.iscas.pm.api.mapper.projectPlan.TaskFeedbackMapper;
import com.iscas.pm.api.model.projectPlan.PlanTask;
import com.iscas.pm.api.model.projectPlan.TaskFeedback;
import com.iscas.pm.api.model.projectPlan.TaskStatusEnum;
import com.iscas.pm.api.service.ProjectPlanService;
import com.iscas.pm.api.service.TaskFeedbackService;
import com.iscas.pm.api.util.DateUtil;
import com.iscas.pm.common.core.util.TreeUtil;
import com.iscas.pm.common.core.web.exception.SimpleBaseException;
import com.iscas.pm.common.db.separate.datasource.DynamicDataSource;
import org.apache.ibatis.jdbc.ScriptRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.sql.SQLException;
import java.util.*;
import java.util.function.Function;
import java.util.stream.Collectors;

/**
 * @author lichang
 * @description 项目计划实现类
 * @createDate 2022-07-28 17:13:09
 */
@Service
public class ProjectPlanServiceImpl extends ServiceImpl<ProjectPlanMapper, PlanTask> implements ProjectPlanService {
    @Autowired
    ProjectPlanMapper projectPlanMapper;
    @Autowired
    TaskFeedbackService taskFeedbackService;
    @Autowired
    TaskFeedbackMapper taskFeedbackMapper;
    @Autowired
    DynamicDataSource dynamicDataSource;

    @Override
    public List<PlanTask> getTaskList() {
        //数据库查询结果转成树结构
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
        planTask.setPersonCount(planTask.getWorkerList() == null ? null : planTask.getWorkerList().size());
        planTask.setStatus(getStatus(planTask));
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

        //查询该计划任务的所有反馈
        List<TaskFeedback> taskFeedbacks = taskFeedbackService.selectListByTaskId(new TaskFeedback().setPlanTaskId(planTask.getId()));

        //如果已经存在反馈人，反馈人必须包含在责任人中
        List<Integer> userIds = taskFeedbacks.stream().map(TaskFeedback::getUserId).distinct().collect(Collectors.toList());
        if (userIds.size() > 0 && (planTask.getWorkerIds() == null || !planTask.getWorkerIds().containsAll(userIds)))
            throw new IllegalArgumentException("已经填写了反馈的人员必须包含在责任人中");

        List<PlanTask> updatedPlanTasks = new ArrayList<>();

        //移动任务，父任务未发生变化
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
        //移动任务，父任务发生变化
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

        //更新当前修改的任务
        planTask.setPersonCount(planTask.getWorkerList() == null ? null : planTask.getWorkerList().size());
        planTask.setStatus(getStatus(planTask));
        planTask.setWorkingDays(DateUtil.daysBetween(planTask.getStartDate(), planTask.getEndDate()));
        planTask.setWbs(parent == null ? planTask.getPosition().toString() : parent.getWbs() + "." + planTask.getPosition());
        planTask.setProgressRate(db_task.getProgressRate());
        updatedPlanTasks.add(planTask);

        //更新当前修改的任务下的所有子任务的wbs
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

        //重新计算任务的已发生工时、任务进度、任务实际开始时间、任务结束时间、任务状态
        computePlanTask(planTask, taskFeedbacks);
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

        //待删除任务之后的任务前移
        List<PlanTask> updatedPlanTasks = movePosition(id, task.getParentId(), task.getPosition(), null, true);
        if (updatedPlanTasks.size() > 0)
            super.updateBatchById(updatedPlanTasks);

        //删除任务
        projectPlanMapper.deleteById(id);
        return true;
    }

    @Override
    public List<PlanTask> getTaskListByWbs() {
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
    public void computePlanTask(PlanTask planTask, List<TaskFeedback> taskFeedbacks) {
        if (taskFeedbacks.size() > 0) {
            //计算任务的实际发生工时=所有反馈工时的和
            Double happenedHour = taskFeedbacks.stream().collect(Collectors.summingDouble(TaskFeedback::getWorkingHour));
            planTask.setHappenedHour(happenedHour);

            //计算任务实际开始时间=最早反馈日期
            planTask.setActualStartDate(taskFeedbacks.get(0).getDate());

            //计算任务进度=所有人进度最大值求和/人数
            Integer progress_total = taskFeedbacks.stream().collect(Collectors.groupingBy(TaskFeedback::getUserId, Collectors.summarizingInt(TaskFeedback::getProgress))).entrySet().stream().mapToInt(t -> t.getValue().getMax()).sum();
            Integer progress = progress_total / planTask.getPersonCount();
            planTask.setProgressRate(progress);

            //任务完成
            if (planTask.getProgressRate() == 100) {
                //计算任务实际结束时间=最晚反馈日期
                planTask.setActualEndDate(taskFeedbacks.get(taskFeedbacks.size() - 1).getDate());

                //计算任务状态:计划结束日期>=实际结束时间 已完成；否则延迟完成
                if (planTask.getEndDate() == null || planTask.getEndDate().after(planTask.getActualEndDate())) {
                    planTask.setStatus(TaskStatusEnum.FINISHED);
                } else {
                    planTask.setStatus(TaskStatusEnum.DELAYED_FINISH);
                }
            }
            //任务未完成
            else {
                planTask.setActualEndDate(null);
                if (planTask.getEndDate() == null || new Date().before(planTask.getEndDate())) {
                    planTask.setStatus(TaskStatusEnum.RUNNING);
                } else {
                    planTask.setStatus(TaskStatusEnum.DELAYED);
                }
            }

            //更新计划任务
            projectPlanMapper.updateById(planTask);

        }
    }

    @Override
    public Boolean importTemplate(String type) {
        //若计划任务已经存在任务反馈，不允许导入模板
        if (taskFeedbackMapper.selectList(new QueryWrapper<TaskFeedback>().isNotNull("plan_task_id")).size() > 0){
            throw new IllegalArgumentException("当前计划已经填写过反馈，不允许通过模板导入计划");
        }

        try {
            ScriptRunner runner = new ScriptRunner(dynamicDataSource.getConnection());

            //自动提交
            runner.setAutoCommit(true);
            runner.setFullLineDelimiter(false);
            //每条命令间的分隔符
            runner.setDelimiter(";");
            runner.setSendFullScript(false);
            runner.setStopOnError(false);

            //如果有多个sql文件，可以写多个runner.runScript(xxx)
            runner.runScript(new InputStreamReader(this.getClass().getClassLoader().getResourceAsStream("template/project_plan_" + type + ".sql"), "utf-8"));
        } catch (SQLException e) {
            e.printStackTrace();
            throw new SimpleBaseException(500, "sql模板文件语法错误");
        } catch (UnsupportedEncodingException e) {
            e.printStackTrace();
            throw new SimpleBaseException(500, "sql模板文件编码格式错误");
        } catch (Exception e){
            e.printStackTrace();
            throw new SimpleBaseException(500, "模板文件【template/project_plan_" + type + ".sql】不存在");
        }
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
                .ne(id != null, "id", id);
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

    private TaskStatusEnum getStatus(PlanTask planTask) {
        if (planTask.getStartDate() == null || planTask.getEndDate() == null)
            return null;
        if (new Date().before(planTask.getStartDate()))
            return TaskStatusEnum.UN_START;
        else if (planTask.getStartDate().before(new Date()) && new Date().before(planTask.getEndDate()))
            return TaskStatusEnum.RUNNING;
        else
            return TaskStatusEnum.DELAYED;
    }

}




