package com.iscas.pm.api.service;

import com.iscas.pm.api.domain.Project;

import java.util.List;

/**
* @author 66410
* @description 针对表【pjdata】的数据库操作Service
* @createDate 2022-07-04 13:44:25
*/
public interface ProjectInfoService  {

    List<Project> findProjectInfoById(String id);
}
