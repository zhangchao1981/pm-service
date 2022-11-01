package com.iscas.pm.api.service.impl.dev;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.dev.DevRequirementMapper;
import com.iscas.pm.api.mapper.dev.DevTaskMapper;
import com.iscas.pm.api.model.dev.*;
import com.iscas.pm.api.model.projectPlan.TaskFeedback;
import com.iscas.pm.api.model.projectPlan.TaskStatusEnum;
import com.iscas.pm.api.service.DevModularService;
import com.iscas.pm.api.service.DevRequirementService;
import com.iscas.pm.api.service.DevTaskService;
import com.iscas.pm.common.core.util.TreeUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.*;
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
    @Autowired
    DevModularService devModularService;

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

            //需求完成
            if (devRequirement.getDevProgress() == 100) {
                //计算需求实际结束时间=最晚反馈日期
                Optional<DevTask> max = devTasks.stream().filter(devTask -> devTask.getActualStartDate() != null).max(Comparator.comparing(DevTask::getActualEndDate));
                Date actualEndDate = max.isPresent() ? max.get().getActualEndDate() : null;
                devRequirement.setActualEndDate(actualEndDate);

                //计算需求状态:计划结束日期>=实际结束时间 已完成；否则延迟完成
                if (devRequirement.getEndDate() == null || devRequirement.getEndDate().after(devRequirement.getActualEndDate())) {
                    devRequirement.setStatus(RequireStatusEnum.FINISHED);
                } else {
                    devRequirement.setStatus(RequireStatusEnum.DELAYED_FINISH);
                }
            }
            //需求未完成
            else {
                devRequirement.setActualEndDate(null);
                if (devRequirement.getEndDate() == null || new Date().before(devRequirement.getEndDate())) {
                    devRequirement.setStatus(RequireStatusEnum.DEVELOPING);
                } else {
                    devRequirement.setStatus(RequireStatusEnum.DELAYED);
                }
            }

            //更新需求
            super.updateById(devRequirement);
        }

    }

    @Override
    public List<DevModular> devRequirementListOrTaskList(DevRequirementQueryParam queryParam) {
        List<DevModular> devModulars;
        if ("DevRequirement".equals(queryParam.getQueryType())) {
            //查询开发需求
            QueryWrapper<DevRequirement> requirementQueryWrapper = new QueryWrapper<>();
            requirementQueryWrapper.ge(queryParam.getStartDate() != null, "start_date", queryParam.getStartDate())
                    .le(queryParam.getEndDate() != null, "end_date", queryParam.getEndDate())
                    .in(queryParam.getWorkerList() != null && queryParam.getWorkerList().size() > 0, "worker", queryParam.getWorkerList())
                    .eq(queryParam.getRequireStatus() != null, "status", queryParam.getRequireStatus())
                    .and(queryParam.getNameOrCode() != null, wapper -> wapper.like("id", queryParam.getNameOrCode())
                            .or().like("name", queryParam.getNameOrCode()));
            List<DevRequirement> devRequirementList = list(requirementQueryWrapper);
            if (devRequirementList.size() < 1) {
                return null;
            }
            Map<Integer, List<DevRequirement>> requirementGroup = devRequirementList.stream()
                    .collect(Collectors.groupingBy(DevRequirement::getModularId));
            List<Integer> modularIds = devRequirementList.stream().map(DevRequirement::getModularId).collect(Collectors.toList());

            //查询目标开发需求所在模块
            QueryWrapper<DevModular> modularQueryWrapper = new QueryWrapper<>();
            //需求A，B C-->模块 1 2   映射关系在require里面存的有

            List<DevModular> allModular = devModularService.list();

            List<DevModular> modularList=new ArrayList<>();
            allModular.forEach(eachModular->{
                if (modularIds.contains(eachModular.getId())){
                    addModularTree(eachModular.getParentId(),modularList,allModular);
                    modularList.add(eachModular.setDevRequirements(requirementGroup.get(eachModular.getId())));
                }
            });
//            modularQueryWrapper.in(modularIds.size() > 0, "id", modularIds).orderByAsc("id");
//            List<DevModular> modularList = devModularService.list(modularQueryWrapper);

//            modularList.forEach(modular -> {
////                if ()
////                modularList.add()
//                modular.setDevRequirements(requirementGroup.get(modular.getId()));
//            });
            devModulars = TreeUtil.treeOut(modularList, DevModular::getId, DevModular::getParentId, DevModular::getModulars);
        } else {
            //查询开发任务
            QueryWrapper<DevTask> taskQueryWrapper = new QueryWrapper<>();
            taskQueryWrapper.ge(queryParam.getStartDate() != null, "start_date", queryParam.getStartDate())
                    .le(queryParam.getEndDate() != null, "end_date", queryParam.getEndDate())
                    .in(queryParam.getWorkerList() != null && queryParam.getWorkerList().size() > 0, "worker", queryParam.getWorkerList())
                    .eq(queryParam.getTaskStatus() != null, "status", queryParam.getTaskStatus())
                    .and(queryParam.getNameOrCode() != null,wrapper -> wrapper.like( "id", queryParam.getNameOrCode())
                            .or().like( "name", queryParam.getNameOrCode()));
            List<DevTask> devTaskList = devTaskService.list(taskQueryWrapper);
            if (devTaskList.size() < 1) {
                return null;
            }
            List<Integer> devRequirementIds = devTaskList.stream().map(DevTask::getRequireId).distinct().collect(Collectors.toList());
            Map<Integer, List<DevTask>> devTaskGroup = devTaskList.stream().collect(Collectors.groupingBy(DevTask::getRequireId));

            //查询目标开发任务所在需求
            QueryWrapper<DevRequirement> requirementQueryWrapper = new QueryWrapper<>();
            requirementQueryWrapper.in(devRequirementIds.size() > 0, "id", devRequirementIds);
            List<DevRequirement> requirementList = list(requirementQueryWrapper);
            requirementList.forEach(requirement -> {
                requirement.setDevTasks(devTaskGroup.get(requirement.getId()));
            });
            Map<Integer, List<DevRequirement>> requirementGroup = requirementList.stream().collect(Collectors.groupingBy(DevRequirement::getModularId));
            //查询需求所在模块
            List<Integer> modularIds = requirementList.stream().map(DevRequirement::getModularId).distinct().collect(Collectors.toList());
            //查询目标开发需求所在模块
            QueryWrapper<DevModular> modularQueryWrapper = new QueryWrapper<>();
            //需求A，B C-->模块 1 2   映射关系在require里面存的有
            List<DevModular> allModular = devModularService.list();

            List<DevModular> modularList=new ArrayList<>();
            allModular.forEach(eachModular->{
                if (modularIds.contains(eachModular.getId())){
                    addModularTree(eachModular.getParentId(),modularList,allModular);
                    modularList.add(eachModular.setDevRequirements(requirementGroup.get(eachModular.getId())));
                }
            });
            devModulars = TreeUtil.treeOut(modularList, DevModular::getId, DevModular::getParentId, DevModular::getModulars);
        }
        return devModulars;
    }

    private void addModularTree(Integer parentId, List<DevModular> modularList, List<DevModular> allModular) {

        //有父节点
        if (parentId!=null && parentId!=0){

            //找到父节点对象
            DevModular modular=null ;
            for (DevModular devModular : allModular) {
                if (parentId.equals(devModular.getId())) {
                    modular=devModular;
                    break;}
            }
            addModularTree(modular.getParentId(),modularList,allModular);
            if (modularList.contains(modular)){
                return;
            }
            modularList.add(modular);
        }

        //没有父节点
        return;
    }
}




