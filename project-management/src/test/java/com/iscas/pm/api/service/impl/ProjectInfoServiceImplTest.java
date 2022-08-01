//package com.iscas.pm.api.service.impl;
//
//import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
//import com.iscas.pm.api.mapper.project.InitializationMapper;
//import com.iscas.pm.api.mapper.project.ProjectMapper;
//import com.iscas.pm.api.model.project.Project;
//import com.iscas.pm.api.model.project.ProjectQueryParam;
//import com.iscas.pm.api.model.project.ProjectStatusEnum;
//import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.boot.test.context.SpringBootTest;
//import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
//
//import javax.annotation.Resource;
//import java.util.List;
//
///**
// * @Author： zhangchao
// * @Date： 2022/7/18
// * @Description：
// */
//@SpringBootTest
//@RunWith(SpringJUnit4ClassRunner.class)
//public class ProjectInfoServiceImplTest{
//    @Resource
//    private ProjectMapper projectMapper;
//    @Autowired
//    private InitSchemaServiceImpl initSchemaService;
//
//    @Test
//    public void testSelect(){
////        ProjectQueryParam projectQueryParam = new ProjectQueryParam();
////        projectQueryParam.setProjectName("te");
////        projectQueryParam.setUserId(2);projectQueryParam.setStatus(ProjectStatusEnum.CLOSED.name());
////        Page<Project> page = new Page<>(1, 1);
////        Page<Project> projectList = projectMapper.getProjectList(page,projectQueryParam);
////        System.out.println(projectList.toString());
//
//        initSchemaService.initSchema("project6");
//        System.out.println(1111);
//    }
//
//
//}