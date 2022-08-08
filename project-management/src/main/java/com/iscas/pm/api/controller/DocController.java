package com.iscas.pm.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iscas.pm.api.model.doc.Directory;
import com.iscas.pm.api.model.doc.Document;
import com.iscas.pm.api.model.doc.ReferenceDoc;
import com.iscas.pm.api.model.doc.ReviseRecord;
import com.iscas.pm.api.service.*;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.List;

/**
 * @author by  lichang
 * @date 2022/7/29.
 */
@RestController
@Api(tags = {"项目文档"})
@RequestMapping("/projectDoc")
@ApiSort(5)
public class DocController {

    @Autowired
    DirectoryService directoryService;
    @Autowired
    DocumentService documentService;
    @Autowired
    ReferenceDocService referenceDocService;
    @Autowired
    ReviseRecordService reviseRecordService;
    @Autowired
    DocTemplateService docTemplateService;


    @GetMapping("/findDirectory")
    @ApiOperation(value = "查询目录树", notes = "查询整棵目录树")
    @ApiOperationSupport(order = 1)
    @PreAuthorize("hasAuthority('/projectDoc/findDirectory')")
    public List<Directory> getAll() {
        return directoryService.getDirectoryTree();
    }

    @PostMapping("/addDirectory")
    @ApiOperation(value = "添加目录", notes = "不允许重名")
    @ApiOperationSupport(order = 2)
    @PreAuthorize("hasAuthority('/projectDoc/addDirectory')")
    public Directory addDirectory(@Valid @RequestBody Directory directory) {
        return directoryService.addDirectory(directory);
    }

    @PostMapping("/deleteDirectory")
    @ApiOperation(value = "删除目录", notes = "根据目录id删除")
    @ApiOperationSupport(order = 3)
    @PreAuthorize("hasAuthority('/projectDoc/deleteDirectory')")
    public boolean deleteDirectory(@NotNull(message = "目录Id不能为空") @RequestParam Integer id) {
        //首先判断是否有子目录
        if (directoryService.list(new QueryWrapper<com.iscas.pm.api.model.doc.Directory>().eq(id != 0, "parent_id", id)).size() > 0) {
            throw new IllegalArgumentException("该目录仍有子目录存在，不允许删除");
        }
        //没有子目录，直接判断有无文档
        if (documentService.list(new QueryWrapper<Document>().eq("directory_id", id)).size() > 0) {
            throw new IllegalArgumentException("该目录下有文档存在，不允许删除");
        }
        return directoryService.deleteDirectory(id);
    }


    /**
     * 待测试 ： 不更改其id及父id
     */
    @PostMapping("/editDirectory")
    @ApiOperation(value = "修改目录", notes = "修改目录名称(或父id)")
    @ApiOperationSupport(order = 4)
    @PreAuthorize("hasAuthority('/projectDoc/editDirectory')")
    public Directory editDirectory(@Valid @RequestBody Directory directory) {
        return directoryService.editDirectory(directory);
    }

    @PostMapping("/addLocalDocument")
    @ApiOperation(value = "添加本地文档", notes = "上传本地文档到服务器")
    @ApiImplicitParam(name = "documentJson", value = "前端封装成json字符串，参见Model对象Document")
    @ApiOperationSupport(order = 11)
    @PreAuthorize("hasAuthority('/projectDoc/addLocalDocument')")
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
    @ApiOperationSupport(order = 12)
    @PreAuthorize("hasAuthority('/projectDoc/addLinkDocument')")
    public Document addLinkDocument(@Valid @RequestBody Document document) {
        if (StringUtils.isBlank(document.getPath())) {
            throw new IllegalArgumentException("文档路径不能为空");
        }
        document.setCreateTime(new Date());
        document.setUpdateTime(new Date());
        document.setUploader(RequestHolder.getUserInfo().getEmployeeName());
        documentService.addLinkDocument(document);
        return document;
    }


    @PostMapping("/deleteDocument")
    @ApiOperation(value = "删除文档")
    @ApiOperationSupport(order = 13)
    @PreAuthorize("hasAuthority('/projectDoc/deleteDocument')")
    public boolean deleteDocument(@NotNull @RequestParam Integer documentId) {
        if (!documentService.removeById(documentId)) {
            throw new IllegalArgumentException("删除失败，文档不存在");
        }
        return true;
    }

    @PostMapping("/deleteDocumentBatch")
    @ApiOperation(value = "批量删除文档", notes = "参数不可为空,删除参数list中所有id对应的文档")
    @ApiOperationSupport(order = 14)
    @PreAuthorize("hasAuthority('/projectDoc/deleteDocumentBatch')")
    public boolean deleteBatchDocument(@NotEmpty(message = "参数Id列表不能为空") @RequestParam List<Integer> docIdList) {
        if (!documentService.remove(new QueryWrapper<Document>().in("id", docIdList))) {
            throw new IllegalArgumentException("删除失败，文档不存在");
        }
        return true;
    }


    @GetMapping("/downloadDocument")
    @ApiOperation(value = "下载文档", notes = "本地上传文档和系统生成文档支持下载，链接类型文档不支持下载")
    @ApiOperationSupport(order = 15)
    @PreAuthorize("hasAuthority('/projectDoc/downloadDocument')")
    public Document downloadDocument(Integer directoryId) {
        Document document = documentService.getById(directoryId);
        if (document == null) {
            throw new IllegalArgumentException("要下载的文件ID不存在");
        }
        return document;
    }

    @PostMapping("/addReferenceDoc")
    @ApiOperation(value = "添加引用文档", notes = "templateId不存在则抛出 不符合数据库约束性，导致异常 ")
    @ApiOperationSupport(order = 21)
    @PreAuthorize("hasAuthority('/projectDoc/addReferenceDoc')")
    public Boolean addReferenceDoc(@Valid @RequestBody ReferenceDoc referenceDoc) {
        if (documentService.getById(referenceDoc.getTemplateId()) == null) {
            throw new IllegalArgumentException("引用文档templateId对应模板不存在");
        }
        return referenceDocService.save(referenceDoc);
    }

    @PostMapping("/editReferenceDoc")
    @ApiOperation(value = "修改引用文档")
    @ApiOperationSupport(order = 22)
    @PreAuthorize("hasAuthority('/projectDoc/editReferenceDoc')")
    public boolean editReferenceDoc(@Valid @RequestBody ReferenceDoc referenceDoc) {
        if (documentService.getById(referenceDoc.getTemplateId()) == null) {
            throw new IllegalArgumentException("引用文档templateId对应模板不存在");
        }
        return referenceDocService.updateById(referenceDoc);
    }

    @PostMapping("/referenceDocList")
    @ApiOperation(value = "查询引用文档", notes = "")
    @ApiOperationSupport(order = 23)
    @PreAuthorize("hasAuthority('/projectDoc/referenceDocList')")
    public List<ReferenceDoc> referenceDocList(@NotNull(message = "引用文档Id不能为空") @RequestParam Integer templateId) {
        return referenceDocService.list(new QueryWrapper<ReferenceDoc>().eq("template_id", templateId));
    }


    @PostMapping("/deleteReferenceDoc")
    @ApiOperation(value = "删除引用文档", notes = "")
    @ApiOperationSupport(order = 24)
    @PreAuthorize("hasAuthority('/projectDoc/deleteReferenceDoc')")
    public boolean deleteReferenceDoc(@NotEmpty(message = "id不能为空") @RequestParam Integer referenceId) {
       if (!referenceDocService.remove(new QueryWrapper<ReferenceDoc>().eq("id", referenceId))){
           throw new IllegalArgumentException("要删除的引用文档不存在");
       }
        return true;
    }

    @PostMapping("/addReviseRecord")
    @ApiOperation(value = "添加修订记录", notes = "")
    @ApiOperationSupport(order = 31)
    @PreAuthorize("hasAuthority('/projectDoc/addReviseRecord')")
    public Boolean addReviseRecord(@Valid @RequestBody ReviseRecord reviseRecord) {
        if (documentService.getById(reviseRecord.getTemplateId()) == null) {
            throw new IllegalArgumentException("引用文档templateId对应模板不存在");
        }
        return reviseRecordService.save(reviseRecord);
    }

    @PostMapping("/editReviseRecord")
    @ApiOperation(value = "修改修订记录", notes = "")
    @ApiOperationSupport(order = 32)
    @PreAuthorize("hasAuthority('/projectDoc/editReviseRecord')")
    public boolean editReviseRecord(@Valid @RequestBody ReviseRecord reviseRecord) {
        if (documentService.getById(reviseRecord.getTemplateId()) == null) {
            throw new IllegalArgumentException("引用文档templateId对应模板不存在");
        }
        if (!reviseRecordService.updateById(reviseRecord)) {
            throw new IllegalArgumentException("要修改的修订记录id不存在");
        }
        return true;
    }

    @PostMapping("/ReviseRecordList")
    @ApiOperation(value = "查询修订记录", notes = "")
    @ApiOperationSupport(order = 33)
    @PreAuthorize("hasAuthority('/projectDoc/ReviseRecordList')")
    public List<ReviseRecord> reviseRecordList(@NotNull(message = "templateId不能为空") @RequestParam Integer templateId) {
        return reviseRecordService.list(new QueryWrapper<ReviseRecord>().eq("template_id", templateId));
    }

    @PostMapping("/deleteReviseRecord")
    @ApiOperation(value = "删除修订记录")
    @ApiOperationSupport(order = 34)
    @PreAuthorize("hasAuthority('/projectDoc/deleteReviseRecord')")
    public boolean deleteReviseRecord(@NotEmpty(message = "id不能为空") @RequestParam Integer reviseRecordId) {
           if ( !reviseRecordService.remove(new QueryWrapper<ReviseRecord>().eq("id", reviseRecordId))){
               throw new IllegalArgumentException("要删除的修订记录不存在");
           }   return true;

    }

    @PostMapping("/deleteTemplate")//没添加权限
    @ApiOperation(value = "删除文档模板", notes = "待开发")
    @ApiOperationSupport(order = 41)
    @PreAuthorize("hasAuthority('/projectDoc/deleteTemplate')")
    public boolean deleteTemplate(@NotNull(message = "模板id不能为空") @RequestParam List<Integer> idList) {
        return true;
    }

}
