package com.iscas.pm.api.service.impl;

import com.iscas.pm.api.mapper.ProjectMapper;
import com.iscas.pm.api.model.project.Project;
import com.iscas.pm.api.model.project.ProjectType;
import com.iscas.pm.api.model.project.SecretLevel;
import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import javax.annotation.Resource;
import java.util.List;

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

    @Test
    public void testSelect(){
        DataSourceHolder.setDB("project1");
        List<Project> projects = projectMapper.selectList(null);
        System.out.println(projects.toString());
    }


}