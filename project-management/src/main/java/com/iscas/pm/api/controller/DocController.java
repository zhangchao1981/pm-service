package com.iscas.pm.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iscas.pm.api.model.project.Directory;
import com.iscas.pm.api.model.project.Document;
import com.iscas.pm.api.model.project.ReferenceDoc;
import com.iscas.pm.api.model.project.ReviseRecord;
import com.iscas.pm.api.service.DirectoryService;
import com.iscas.pm.api.service.DocumentService;
import com.iscas.pm.api.service.ReferenceDocService;
import com.iscas.pm.api.service.ReviseRecordService;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import io.swagger.annotations.Api;
import io.swagger.annotations.ApiImplicitParam;
import io.swagger.annotations.ApiOperation;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author by  lichang
 * @date 2022/7/29.
 */
@RestController
@Api(tags = {"项目基本信息"})
@RequestMapping("/projectDoc")
public class DocController {
    @Autowired
    DirectoryService directoryService;

    @Autowired
    DocumentService documentService;

    @Autowired
    ReferenceDocService referenceDocService;

    @Autowired
    ReviseRecordService reviseRecordService;

    @PostMapping("/addDirectory")
    @ApiOperation(value = "添加目录", notes = "")
//    @PreAuthorize("hasAuthority('/projectDoc/addDirectory')")
    public Directory addDirectory(@Valid @RequestBody Directory directory) {
        return directoryService.addDirectory(directory
        );
    }

    @GetMapping("/findDirectory")
    @ApiOperation(value = "查找目录", notes = "")
//    @PreAuthorize("hasAuthority('/projectDoc/findDirectory')")
    public List<Directory> getAll(@NotNull @RequestParam Integer id) {
        return directoryService.getDirectoryTree(id, null);
    }


    @PostMapping("/deleteDirectory")
    @ApiOperation(value = "删除目录", notes = "")
//    @PreAuthorize("hasAuthority('/projectDoc/deleteDirectory')")
    public boolean deleteDirectory(@NotNull @RequestParam Integer id) {
        return directoryService.deleteDirectory(id);
    }

    @PostMapping("/editDirectory")
    @ApiOperation(value = "修改目录", notes = "不允许改id, 要求前端传过来的是要修改的值 ")
//    @PreAuthorize("hasAuthority('/projectDoc/editDirectory')")
    public Directory editDirectory(@Valid @RequestBody Directory directory) {
        //需测试 调接口但是不修改
        return directoryService.editDirectory(directory);
    }




    @PostMapping("/addLocalDocument")
    @ApiOperation(value = "添加本地文档", notes = "上传本地文档到服务器")
    @ApiImplicitParam(name = "documentJson", value = "前端封装成json字符串，参见Model对象Document")
//    @PreAuthorize("hasAuthority('/projectDoc/addLocalDocument')")
    public Document addLocalDocument(MultipartFile file, String documentJson) throws IOException {
        @Valid Document document = JSONObject.parseObject(documentJson, Document.class);

        document.setCreateTime(new Date());
        document.setUpdateTime(new Date());
        document.setUploader(RequestHolder.getUserInfo().getEmployeeName());
        documentService.addLocalDocument(file, document);

        return document;
    }

    @PostMapping("/addLinkDocument")
    @ApiOperation(value = "添加链接文档", notes = "上传本地文档到服务器")
    @PreAuthorize("hasAuthority('/projectDoc/addLinkDocument')")
    public Document addLinkDocument(@Valid @RequestBody Document document) {
        if (StringUtils.isBlank(document.getPath()))
            throw new IllegalArgumentException("文档路径不能为空");

        document.setCreateTime(new Date());
        document.setUpdateTime(new Date());
        document.setUploader(RequestHolder.getUserInfo().getEmployeeName());
        documentService.addLinkDocument(document);
        return document;
    }

    @PostMapping("/editDocument")
    @ApiOperation(value = "修改文档信息", notes = "修改文档信息，文档类型不能修改")
    @PreAuthorize("hasAuthority('/projectDoc/editDocument')")
    public Boolean editDocument(@Valid @RequestBody Document document) {
        return documentService.editDocument(document);
    }


    @PostMapping("/deleteDocument")
    @ApiOperation(value = "删除文档", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/deleteDocument')")
    public Document deleteDocument(@Valid @RequestBody Document document) {
        documentService.removeById(document.getId());
        return document;
    }

    @PostMapping("/deleteDocumentBatch")
    @ApiOperation(value = "批量删除文档", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/deleteDocument')")
    public boolean deleteBatchDocument(List<Integer> docIdList) {
        return documentService.remove(new QueryWrapper<Document>().in("id", docIdList));
    }

    @GetMapping("/downloadDocument")
    @ApiOperation(value = "下载文档", notes = "本地上传文档和系统生成文档支持下载，链接类型文档不支持下载")
    @PreAuthorize("hasAuthority('/projectDoc/downloadDocument')")
    public List<Document> downloadDocument(Integer directoryId, String documentName) {
        return documentService.getDocuments(directoryId, documentName);
    }


    @PostMapping("/deleteReferenceDoc")
    @ApiOperation(value = "删除引用文档", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/deleteReferenceDoc')")
    public boolean deleteReferenceDoc(@NotNull @RequestParam List<Integer> idList) {
        QueryWrapper<ReferenceDoc> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", idList);
        return referenceDocService.remove(queryWrapper);
    }

    @PostMapping("/editReferenceDoc")
    @ApiOperation(value = "修改引用文档", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/editReferenceDoc')")
    public boolean editReferenceDoc(@Valid @RequestBody ReferenceDoc referenceDoc) {
        //重名提示？
        QueryWrapper<ReferenceDoc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("name", referenceDoc.getName());
            return referenceDocService.saveOrUpdate(referenceDoc);
    }




}
