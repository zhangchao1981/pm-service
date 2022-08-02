package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.iscas.pm.api.mapper.projectPlan.ProjectPlanMapper;
import com.iscas.pm.api.model.projectPlan.PlanTask;
import com.iscas.pm.api.service.ProjectPlanService;
import com.iscas.pm.api.util.DateUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

/**
 * @author lichang
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
        //转成树结构
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

        //新任务之后的任务后移
        movePosition(parentId,position,null,false);

        //插入新任务到数据库
        planTask.setWorkingDays(DateUtil.daysBetween(planTask.getStartDate(), planTask.getEndDate()));
        planTask.setWbs(parent == null ? position.toString() : parent.getWbs() + "." + position);
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

        //父任务未发生变化
        if (planTask.getParentId() == db_task.getParentId()) {
            //排序位置前移
            if (planTask.getPosition() < db_task.getPosition()){
                movePosition(planTask.getParentId(), planTask.getPosition(), db_task.getPosition(),true);
            }
            //排序位置后移
            else if (planTask.getPosition() > db_task.getPosition()){
                movePosition(planTask.getParentId(), db_task.getPosition(), planTask.getPosition(),false);
            }
        }
        //父任务发生变化
        else{
            //原父任务下子任务前移
            movePosition(db_task.getParentId(), db_task.getPosition(), null,true);
            //新父任务下子任务后移
            movePosition(planTask.getParentId(), planTask.getPosition(), null,false);
        }

        //更新任务
        planTask.setWorkingDays(DateUtil.daysBetween(planTask.getStartDate(), planTask.getEndDate()));
        planTask.setWbs(parent == null ? planTask.getPosition().toString() : parent.getWbs() + "." + planTask.getPosition());
        projectPlanMapper.updateById(planTask);
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

        //待删除任务以后的任务前移
        movePosition(task.getParentId(), task.getPosition(), null,true);

        //删除任务
        projectPlanMapper.deleteById(id);

        return true;
    }

    /**
     * 移动排序位置
     *
     * @param parentId  父任务id
     * @param startPosition  受影响记录的起始位置
     * @param endPosition   受影响记录的结束位置
     * @param isForward 是否是前移
     */
    private void movePosition(Integer parentId, Integer startPosition,Integer endPosition, boolean isForward) {
        //查询出受排序影响的任务列表
        QueryWrapper<PlanTask> queryWrapper = new QueryWrapper<PlanTask>()
                .eq("parentId", parentId)
                .ge(startPosition != null,"position", startPosition)
                .le(endPosition != null,"position", endPosition);
        List<PlanTask> planTasks = projectPlanMapper.selectList(queryWrapper);

        //更新受影响的任务列表的wbs和排序编号
        planTasks.forEach(plan -> {
            plan.setPosition(isForward ? plan.getPosition() - 1 : plan.getPosition() + 1);
            String wbs = plan.getWbs().substring(0, plan.getWbs().lastIndexOf("."));
            plan.setWbs(wbs + "." + plan.getPosition().toString());
        });
        super.updateBatchById(planTasks);
    }

}




