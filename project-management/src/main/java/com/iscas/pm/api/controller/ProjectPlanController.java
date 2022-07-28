package com.iscas.pm.api.controller;

import com.iscas.pm.api.model.project.Project;
import com.iscas.pm.api.model.project.ProjectPlan;
import com.iscas.pm.api.service.ProjectPlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * @Author： 李昶
 * @Date： 2022/7/12
 * @Description：项目计划管理
 */
@Api(tags = {"项目计划"})
@RestController
@RequestMapping("/projectPlan")
public class ProjectPlanController {
    @Autowired
    ProjectPlanService projectPlanService;


    @PostMapping("/addProjectPlan")
    @ApiOperation(value = "发布项目计划", notes = "建立一个项目计划")
    @PreAuthorize("hasAuthority('/projectPlan/addProjectPlan')")
    public ProjectPlan addProjectPlan(@Valid @RequestBody ProjectPlan projectPlan){
        projectPlanService.save(projectPlan);
        return  projectPlan;
    }



    /**
     * 待完善
     * @param projectPlan
     * @return
     */
    @PostMapping("/addProjectPlan")
    @ApiOperation(value = "模板导入", notes = "根据模板自动生成一个项目计划")
    @PreAuthorize("hasAuthority('/projectPlan/addProjectPlan')")
    public ProjectPlan importProjectPlan(@Valid @RequestBody ProjectPlan projectPlan){
        projectPlanService.save(projectPlan);


        return  projectPlan;
    }







}
