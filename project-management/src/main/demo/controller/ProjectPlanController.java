package com.demo.controller;

import com.demo.model.project.Plan;
import com.demo.service.PlanService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
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
    PlanService planService;


    @PostMapping("/addProjectPlan")
    @ApiOperation(value = "添加项目计划", notes = "建立一个项目计划")
    @PreAuthorize("hasAuthority('/projectPlan/addProjectPlan')")
    public Plan addProjectPlan(@Valid @RequestBody Plan plan){
        planService.save(plan);
        return  plan;
    }


    /**
     * 待完善
     * @param
     * @return
     */
    @PostMapping("/importTemplate")
    @ApiOperation(value = "模板导入", notes = "根据模板自动生成一个项目计划")
//    @PreAuthorize("hasAuthority('/projectPlan/addProjectPlan')")
    public Plan importByTemplate(@Valid @RequestBody String projectId){

        return  null;
    }







}
