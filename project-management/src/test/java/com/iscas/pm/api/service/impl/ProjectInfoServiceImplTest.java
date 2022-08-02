package com.iscas.pm.api.service.impl;

import com.iscas.pm.api.mapper.project.ProjectMapper;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;

/**
 * @Author： zhangchao
 * @Date： 2022/7/18
 * @Description：
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class ProjectInfoServiceImplTest{
    @Resource
    private ProjectMapper projectMapper;
    @Autowired
    private InitSchemaServiceImpl initSchemaService;

    @Test
    public void testSelect(){

    }


}