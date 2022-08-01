package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.iscas.pm.api.mapper.ProjectPlanMapper;
import com.iscas.pm.api.model.projectPlan.PlanTask;
import com.iscas.pm.api.service.ProjectPlanService;
import com.iscas.pm.api.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author 66410
 * @description 针对表【plan】的数据库操作Service实现
 * @createDate 2022-07-28 17:13:09
 */
@Service
public class ProjectPlanServiceImpl extends ServiceImpl<ProjectPlanMapper, PlanTask> implements ProjectPlanService {
    @Autowired
    ProjectPlanMapper projectPlanMapper;

    @Override
    public List<PlanTask> getTaskList() {

        List<PlanTask> planTasks = super.list();
        return planTasks;
    }

    @Override
    public PlanTask addTask(PlanTask planTask) {
        Integer position = planTask.getPosition();
        Integer parentId = planTask.getParentId();

        //查询父任务信息
        final PlanTask parent = projectPlanMapper.selectById(planTask.getParentId());
        if (parent == null && parentId != 0)
            throw new IllegalArgumentException("父任务不存在");

        //父任务下如果已有反馈，不允许添加子任务


        //查询出受排序影响的任务列表
        QueryWrapper<PlanTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parentId", parentId);
        queryWrapper.ge("position", position);
        List<PlanTask> planTasks = projectPlanMapper.selectList(queryWrapper);

        //更新受影响的任务列表的wbs和排序编号
        planTasks.forEach(plan -> {
            String wbs = parent == null ? position.toString() : parent.getWbs() + "." + position;
            plan.setWbs(wbs);
            plan.setPosition(plan.getPosition() + 1);
        });
        super.updateBatchById(planTasks);

        //插入新任务到数据库
        planTask.setWorkingDays(DateUtil.daysBetween(planTask.getStartDate(),planTask.getEndDate()));
        planTask.setWbs(parent == null ? position.toString() : parent.getWbs() + "." + position);
        projectPlanMapper.insert(planTask);

        return planTask;
    }

    @Override
    public Boolean editTask(PlanTask planTask) {
        //查询数据库中的任务
        PlanTask db_task = projectPlanMapper.selectById(planTask.getId());
        if (db_task == null)
            throw new IllegalArgumentException("修改的任务不存在");

        //父任务未发生变化，只变更该父任务下子任务的排序
        if (planTask.getParentId() == db_task.getParentId()){

        }
        //父任务发生变化，原父任务下的子任务和新父任务下的子任务都变更排序
        return true;
    }

    @Override
    public Boolean deleteTask(String id) {
        PlanTask task = projectPlanMapper.selectById(id);
        if (task == null)
            throw new IllegalArgumentException("待删除任务不存在");

        //有子任务的任务不允许删除
        QueryWrapper<PlanTask> subTaskQuery = new QueryWrapper<>();
        subTaskQuery.eq("parentId", id);
        List<PlanTask> subTasks = projectPlanMapper.selectList(subTaskQuery);
        if (subTasks.size() > 0)
            throw new IllegalArgumentException("该任务下存在子任务，请先删除子任务");

        //任务有关联的反馈记录，不允许删除

        //查询出受排序影响的任务列表
        QueryWrapper<PlanTask> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("parentId", task.getParentId());
        queryWrapper.ge("position", task.getPosition());
        List<PlanTask> planTasks = projectPlanMapper.selectList(queryWrapper);

        //更新受影响的任务列表的wbs和排序编号
        planTasks.forEach(plan -> {
            plan.setPosition(plan.getPosition() - 1);
            String wbs = plan.getWbs().substring(0, plan.getWbs().lastIndexOf("."));
            plan.setWbs(wbs + "." + plan.getPosition().toString());
        });
        super.updateBatchById(planTasks);

        //删除任务
        projectPlanMapper.deleteById(id);

        return true;
    }
}




