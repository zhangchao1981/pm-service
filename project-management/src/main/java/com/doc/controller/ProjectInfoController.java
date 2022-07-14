package com.doc.controller;

import com.doc.domain.ProjectInfo;
import com.doc.service.ProjectInfoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.io.UnsupportedEncodingException;
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
    public ProjectInfo findProjectInfoById(@PathVariable String id){
        return null;
    }


  //加一个controller
    //    //给dataMap对应id ，  自动生成对应的文档
    @PostMapping(value = "/create/{id}")
    public boolean createword(@PathVariable int id) throws UnsupportedEncodingException {
        //首先调用service查找data的方法，然后命名文件名
        String filename="test"+id;
        //生成对应的文件，返回结果是否成功
        return    projectInfoService.createword(id,filename);
    }
}
