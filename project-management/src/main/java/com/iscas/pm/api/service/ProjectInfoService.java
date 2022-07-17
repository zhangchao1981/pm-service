package com.iscas.pm.api.service;

import com.iscas.pm.api.model.project.Project;
import org.springframework.web.bind.annotation.RequestBody;

import javax.validation.Valid;
import java.util.List;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： 项目基本信息管理service接口层
 */
public interface ProjectInfoService  {

   /**
    * 添加项目
    */
   void addProject(Project project);
}
