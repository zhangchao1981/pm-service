package com.iscas.pm.api.controller;

import com.iscas.pm.api.domain.Project;
import com.iscas.pm.api.service.ProjectInfoService;
import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

/**
 * @Author： zhangchao
 * @Date： 2022/7/12
 * @Description： 项目信息维护与查询
 */
@RestController
@RequestMapping("/projectInfo")
public class ProjectInfoController {
    @Autowired
    private ProjectInfoService projectInfoService;

    @GetMapping(value = "/{id}")
    public List<Project> findProjectInfoById(@PathVariable String id){
        //Project project=new Project();
        //project.setId(id);
        DataSourceHolder.setDB("default");
        return projectInfoService.findProjectInfoById(id);
        //return project;
    }
    @GetMapping(value = "/aaa")
    public List<Project> findProjectInfoByIdaa(){
        //Project project=new Project();
        //project.setId(id);
        DataSourceHolder.setDB("project1");
        return projectInfoService.findProjectInfoById("eee");
        //return project;
    }


}
