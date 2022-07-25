package com.iscas.pm.auth.service.impl;

import com.iscas.pm.auth.domain.ProjectPermission;
import com.iscas.pm.auth.service.PmRolePermissionService;
import junit.framework.TestCase;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import java.util.List;

/**
 * @Author： zhangchao
 * @Date： 2022/7/25
 * @Description：
 */
@SpringBootTest
@RunWith(SpringJUnit4ClassRunner.class)
public class PmRolePermissionServiceImplTest extends TestCase {
    @Autowired
    private PmRolePermissionService pmRolePermissionService;

    @Test
    public void testPermissons(){
        List<ProjectPermission> projectPermissionList = pmRolePermissionService.selectProjectPermissions(2);
        System.out.println(projectPermissionList.toString());
    }

}