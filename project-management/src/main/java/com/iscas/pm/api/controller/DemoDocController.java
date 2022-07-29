package com.iscas.pm.api.controller;

import com.iscas.pm.api.model.project.Directory;
import com.iscas.pm.api.service.DirectoryService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;
import java.util.List;

/**
 * @author by  lichang
 * @date 2022/7/29.
 */
@RestController
@Api(tags = {"项目基本信息"})
@RequestMapping("/projectDoc")
public class DemoDocController {
    @Autowired
    DirectoryService directoryService;

    @PostMapping("/addDirectory")
    @ApiOperation(value = "添加目录", notes = "")
//    @PreAuthorize("hasAuthority('/projectDoc/directory')")
    public Directory addDirectory(@Valid @RequestBody Directory directory) {
        return  directoryService.addDirectory(directory);
    }

    @PostMapping("/findDirectory")
    @ApiOperation(value = "查找目录", notes = "")
//    @PreAuthorize("hasAuthority('/projectDoc/directory')")
    public List<Directory> getAll(Integer  id,String name) {
        return  directoryService.getDirectoryTree(id,name) ;
    }


//引用文档和修订记录

    @PostMapping("/finddirectory")
    @ApiOperation(value = "删除目录", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/deleteDirectory')")
    public List<Directory> deleteDirectory(Integer  id,String name) {
        return  directoryService.deleteDirectory(id,name) ;

    }


}
