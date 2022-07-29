package com.iscas.pm.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iscas.pm.api.model.project.Directory;
import com.iscas.pm.api.model.project.Document;
import com.iscas.pm.api.service.DirectoryService;
import com.iscas.pm.api.service.DocumentService;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

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

    @Autowired
    DocumentService documentService;


    @PostMapping("/addDirectory")
    @ApiOperation(value = "添加目录", notes = "")
//    @PreAuthorize("hasAuthority('/projectDoc/addDirectory')")
    public Directory addDirectory(@Valid @RequestBody Directory directory) {
        return directoryService.addDirectory(directory);
    }

    @GetMapping("/findDirectory")
    @ApiOperation(value = "查找目录", notes = "")
//    @PreAuthorize("hasAuthority('/projectDoc/findDirectory')")
    public List<Directory> getAll(Integer id, String name) {
        return directoryService.getDirectoryTree(id, name);
    }


//引用文档和修订记录

    @PostMapping("/deleteDirectory")
    @ApiOperation(value = "删除目录", notes = "")
//    @PreAuthorize("hasAuthority('/projectDoc/deleteDirectory')")
    public List<Directory> deleteDirectory(Integer id, String name) {
        return directoryService.deleteDirectory(id, name);

    }

    @PostMapping("/editDirectory")
    @ApiOperation(value = "修改目录", notes = "不允许改id, 要求前端传过来的是要修改的值 ")
//    @PreAuthorize("hasAuthority('/projectDoc/editDirectory')")
    public Directory editDirectory(@Valid @RequestBody Directory directory) {
        return directoryService.editDirectory(directory);
    }


    @PostMapping("/addDocument")
    @ApiOperation(value = "添加文档", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/addDocument')")
    public Document addDocument(@Valid @RequestBody Document document) {
        documentService.saveOrUpdate(document);
        return document;
    }

    @PostMapping("/editDocument")
    @ApiOperation(value = "修改文档", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/editDocument')")
    public Document editDocument(@Valid @RequestBody Document document) {
        documentService.saveOrUpdate(document);
        return document;
    }


    @PostMapping("/deleteDocument")
    @ApiOperation(value = "删除文档", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/deleteDocument')")
    public Document deleteDocument(@Valid @RequestBody Document document) {
        documentService.removeById(document.getId());
        return document;
    }

    @PostMapping("/deleteDocument")
    @ApiOperation(value = "批量删除文档", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/deleteDocument')")
    public boolean deleteBatchDocument(List<Integer> docIdList) {
        return  documentService.remove(new QueryWrapper<Document>().in("id",docIdList));
    }



    @GetMapping("/getDocument")
    @ApiOperation(value = "查询文档", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/getDocument')")
    public List<Document> getDocument(Integer directoryId, String documentName) {
        return documentService.getDocument(directoryId, documentName);
    }



}
