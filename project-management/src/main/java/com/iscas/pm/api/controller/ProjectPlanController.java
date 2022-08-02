package com.iscas.pm.api.controller;

import com.iscas.pm.api.model.projectPlan.PlanTask;
import com.iscas.pm.api.service.ProjectPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
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
@RestController
@RequestMapping("/projectPlan")
public class ProjectPlanController {
    @Autowired
    ProjectPlanService projectPlanService;

    @GetMapping("/taskList")
    @ApiOperation(value = "查询任务列表", notes = "查询任务列表")
    public List<PlanTask> taskList(){
        return  projectPlanService.getTaskList();
    }

    @PostMapping("/addTask")
    @ApiOperation(value = "添加计划任务", notes = "添加计划任务")
    @PreAuthorize("hasAuthority('/projectPlan/taskManage')")
    public PlanTask addTask(@Valid @RequestBody PlanTask planTask){
        projectPlanService.addTask(planTask);
        return planTask;
    }

    @PostMapping("/editTask")
    @ApiOperation(value = "修改计划任务", notes = "修改指定计划任务")
    @PreAuthorize("hasAuthority('/projectPlan/taskManage')")
    public Boolean editTask(@Valid @RequestBody PlanTask planTask){
        projectPlanService.editTask(planTask);
        return  true;
    }

    @GetMapping("/deleteTask")
    @ApiOperation(value = "删除任务", notes = "删除指定任务，若任务已经填写了反馈，不允许删除")
    @PreAuthorize("hasAuthority('/projectPlan/taskManage')")
    public Boolean deleteTask(String id){
        projectPlanService.deleteTask(id);
        return  true;
    }

    @PostMapping("/importTemplate")
    @ApiOperation(value = "模板导入", notes = "选择模板，将模板内容导入计划表")
    @PreAuthorize("hasAuthority('/projectPlan/taskManage')")
    public Boolean importTemplate(Integer planTemplateId){

        return  null;
    }






}
