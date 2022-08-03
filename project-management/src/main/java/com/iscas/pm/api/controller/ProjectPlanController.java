package com.iscas.pm.api.controller;

import com.iscas.pm.api.model.projectPlan.PlanTask;
import com.iscas.pm.api.model.projectPlan.TaskFeedback;
import com.iscas.pm.api.service.ProjectPlanService;
import com.iscas.pm.api.service.TaskFeedbackService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiSort;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author： 李昶
 * @Date： 2022/7/12
 * @Description： 项目计划管理
 */
@Api(tags = {"项目计划"})
@ApiSort(2)
@RestController
@RequestMapping("/projectPlan")
public class ProjectPlanController {
    @Autowired
    ProjectPlanService projectPlanService;
    @Autowired
    TaskFeedbackService taskFeedbackService;

    @GetMapping("/taskList")
    @ApiOperation(value = "查询任务列表", notes = "查询任务列表")
    public List<PlanTask> taskList(){
        return  projectPlanService.getTaskList();
    }

    @PostMapping("/addTask")
    @ApiOperation(value = "添加计划任务", notes = "添加计划任务")
    @PreAuthorize("hasAuthority('/projectPlan/addTask')")
    public PlanTask addTask(@Valid @RequestBody PlanTask planTask){
        projectPlanService.addTask(planTask);
        return planTask;
    }

    @PostMapping("/editTask")
    @ApiOperation(value = "修改计划任务", notes = "修改指定计划任务")
    @PreAuthorize("hasAuthority('/projectPlan/editTask')")
    public Boolean editTask(@Valid @RequestBody PlanTask planTask){
        projectPlanService.editTask(planTask);
        return  true;
    }

    @GetMapping("/deleteTask")
    @ApiOperation(value = "删除计划任务", notes = "删除指定任务，若任务已经填写了反馈，不允许删除")
    @PreAuthorize("hasAuthority('/projectPlan/deleteTask')")
    public Boolean deleteTask(String id){
        projectPlanService.deleteTask(id);
        return  true;
    }

    @PostMapping("/importTemplate")
    @ApiOperation(value = "计划模板导入（暂缓实现）", notes = "选择模板，将模板内容导入计划表")
    @PreAuthorize("hasAuthority('/projectPlan/addTask')")
    public Boolean importTemplate(Integer planTemplateId){

        return  null;
    }

    @PostMapping("/addTaskFeedback")
    @ApiOperation(value = "添加或修改任务反馈", notes = "添加或修改任务完成情况的反馈信息")
    @PreAuthorize("hasAuthority('/projectPlan/saveTaskFeedback')")
    public TaskFeedback saveTaskFeedback(TaskFeedback taskFeedback){
        taskFeedbackService.saveTaskFeedback(taskFeedback);
        return  taskFeedback;
    }

    @PostMapping("/getTaskFeedbacks")
    @ApiOperation(value = "查询任务反馈", notes = "查询指定任务的反馈列表")
    @PreAuthorize("hasAuthority('/projectPlan/getTaskFeedbacks')")
    public List<TaskFeedback> getTaskFeedbacks(Integer taskId){
        return taskFeedbackService.selectListByPlanTaskId(taskId);
    }

}
