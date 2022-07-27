package com.iscas.pm.auth.service;

import com.iscas.pm.auth.domain.PmRolePermission;
import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.auth.domain.ProjectPermission;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
* @author 66410
* @description 针对表【pm_role_permission】的数据库操作Service
* @createDate 2022-07-19 16:20:19
*/
public interface PmRolePermissionService {

//    List<String> getPermissionsByUserIdandProjectId(Integer userId, Integer projectId);

    List<ProjectPermission> selectProjectPermissions(Integer userId);
}
