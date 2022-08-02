package com.iscas.pm.api.controller;

import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.iscas.pm.api.model.doc.Directory;
import com.iscas.pm.api.model.doc.Document;
import com.iscas.pm.api.model.doc.ReferenceDoc;
import com.iscas.pm.api.model.doc.ReviseRecord;
import com.iscas.pm.api.model.env.EnvInformation;
import com.iscas.pm.api.model.env.EnvSoftware;
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
@ApiSort(1)
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
    EnvInformationService envInformationService;
    @Autowired
    EnvSoftwareService envSoftwareService;

    @PostMapping("/addDirectory")
    @ApiOperation(value = "添加目录", tags = "添加文档目录")
    @ApiOperationSupport(order = 1)
    @PreAuthorize("hasAuthority('/projectDoc/addDirectory')")
    public Directory addDirectory(@Valid @RequestBody Directory directory) {
        return directoryService.addDirectory(directory);
    }

    @GetMapping("/findDirectory")
    @ApiOperation(value = "查找目录", notes = "查询目录列表数")
    @ApiOperationSupport(order = 2)
    @PreAuthorize("hasAuthority('/projectDoc/findDirectory')")
    public List<Directory> getAll() {
        return directoryService.getDirectoryTree();
    }

    @PostMapping("/deleteDirectory")
    @ApiOperation(value = "删除目录", notes = "根据目录id删除")
    @PreAuthorize("hasAuthority('/projectDoc/deleteDirectory')")
    public boolean deleteDirectory(@NotNull(message = "目录Id不能为空") @RequestParam Integer id) {
        return directoryService.deleteDirectory(id);
    }

    @PostMapping("/editDirectory")
    @ApiOperation(value = "修改目录", notes = "不允许改id, 要求前端传过来的是要修改的值 ")
    @PreAuthorize("hasAuthority('/projectDoc/editDirectory')")
    public Directory editDirectory(@Valid @RequestBody Directory directory) {
        return directoryService.editDirectory(directory);
    }

    @PostMapping("/addLocalDocument")
    @ApiOperation(value = "添加本地文档", notes = "上传本地文档到服务器")
    @ApiImplicitParam(name = "documentJson", value = "前端封装成json字符串，参见Model对象Document")
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
    @PreAuthorize("hasAuthority('/projectDoc/deleteDocument')")
    public Document deleteDocument(@Valid @RequestBody Document document) {
        documentService.removeById(document.getId());
        return document;
    }

    @PostMapping("/deleteDocumentBatch")
    @ApiOperation(value = "批量删除文档", notes = "参数不可为空")
    @PreAuthorize("hasAuthority('/projectDoc/deleteDocument')")
    public boolean deleteBatchDocument(@NotEmpty(message = "参数Id列表不能为空") List<Integer> docIdList) {
        return documentService.remove(new QueryWrapper<Document>().in("id", docIdList));
    }

    @GetMapping("/downloadDocument")
    @ApiOperation(value = "下载文档", notes = "本地上传文档和系统生成文档支持下载，链接类型文档不支持下载")
    @PreAuthorize("hasAuthority('/projectDoc/downloadDocument')")
    public List<Document> downloadDocument(Integer directoryId, String documentName) {
        return documentService.getDocuments(directoryId, documentName);
    }

    @PostMapping("/addReferenceDoc")
    @ApiOperation(value = "添加引用文档", notes = "templateId不存在则抛出 不符合数据库约束性，导致异常 ")
    @PreAuthorize("hasAuthority('/projectDoc/addReferenceDoc')")
    public Boolean addReferenceDoc(@Valid @RequestBody ReferenceDoc referenceDoc) {
        return referenceDocService.save(referenceDoc);
    }

    @PostMapping("/editReferenceDoc")
    @ApiOperation(value = "修改引用文档")
    @PreAuthorize("hasAuthority('/projectDoc/editReferenceDoc')")
    public boolean editReferenceDoc(@Valid @RequestBody ReferenceDoc referenceDoc) {
        return referenceDocService.updateById(referenceDoc);

    }

    @PostMapping("/referenceDocList")
    @ApiOperation(value = "查询引用文档", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/referenceDocList')")
    public List<ReferenceDoc> referenceDocList(@NotNull(message = "引用文档Id不能为空") @RequestParam Integer templateId) {
        QueryWrapper<ReferenceDoc> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", templateId);
        return referenceDocService.list(queryWrapper);
    }

    @PostMapping("/deleteReferenceDoc")
    @ApiOperation(value = "删除引用文档", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/deleteReferenceDoc')")
    public boolean deleteReferenceDoc(@NotEmpty(message = "idList不能为空") @RequestParam List<Integer> idList) {
        QueryWrapper<ReferenceDoc> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", idList);
        return referenceDocService.remove(queryWrapper);
    }

    @PostMapping("/addReviseRecord")
    @ApiOperation(value = "添加修订记录", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/addReviseRecord')")
    public Boolean addReviseRecord(@Valid @RequestBody ReviseRecord reviseRecord) {
        return reviseRecordService.save(reviseRecord);
    }

    @PostMapping("/editReviseRecord")
    @ApiOperation(value = "修改修订记录", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/editReviseRecord')")
    public boolean editReviseRecord(@Valid @RequestBody ReviseRecord reviseRecord) {
        return reviseRecordService.updateById(reviseRecord);
    }

    @PostMapping("/ReviseRecordList")
    @ApiOperation(value = "查询修订记录", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/ReviseRecordList')")
    public List<ReviseRecord> reviseRecordList(@NotNull(message = "templateId不能为空") @RequestParam Integer templateId) {
        QueryWrapper<ReviseRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.eq("template_id", templateId);
        return reviseRecordService.list(queryWrapper);
    }

    @PostMapping("/deleteReviseRecord")
    @ApiOperation(value = "删除修订记录", notes = "只要idList里id存在的删，不存在的不删，全部不存在返回false")
    @PreAuthorize("hasAuthority('/projectDoc/deleteReviseRecord')")
    public boolean deleteReviseRecord(@NotNull(message = "idList不能为空") @RequestParam List<Integer> idList) {
        QueryWrapper<ReviseRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", idList);
        return reviseRecordService.remove(queryWrapper);
    }

    @PostMapping("/deleteTemplate")
    @ApiOperation(value = "删除文档模板", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/deleteTemplate')")
    public boolean deleteTemplate(@NotNull(message = "模板id不能为空") @RequestParam List<Integer> idList) {
        QueryWrapper<ReviseRecord> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", idList);
        return reviseRecordService.remove(queryWrapper);
    }


    @PostMapping("/addEnvInformation")
    @ApiOperation(value = "添加环境说明", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/addEnvInformation')")
    public Boolean addEnvInformation(@Valid @RequestBody EnvInformation envInformation) {
        return envInformationService.save(envInformation);
    }


    @PostMapping("/editEnvInformation")
    @ApiOperation(value = "修改环境说明", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/editEnvInformation')")
    public boolean editEnvInformation(@Valid @RequestBody EnvInformation envInformation) {
        return envInformationService.updateById(envInformation);
    }

    @PostMapping("/EnvInformationList")
    @ApiOperation(value = "查询环境说明", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/EnvInformationList')")
    public List<EnvInformation> envInformationList() {
        return envInformationService.list();
    }

    @PostMapping("/deleteEnvInformation")
    @ApiOperation(value = "删除环境说明", notes = "只要idList里id存在的删，不存在的不删，全部不存在返回false")
    @PreAuthorize("hasAuthority('/projectDoc/deleteEnvInformation')")
    public boolean deleteEnvInformation(@NotNull(message = "idList不能为空") @RequestParam List<Integer> idList) {
        QueryWrapper<EnvInformation> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", idList);
        return envInformationService.remove(queryWrapper);
    }


    @PostMapping("/addEnvSoftware")
    @ApiOperation(value = "添加软件环境需求", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/addEnvSoftware')")
    public Boolean addEnvSoftware(@Valid @RequestBody EnvSoftware envSoftware) {
        return envSoftwareService.save(envSoftware);
    }


    @PostMapping("/editEnvSoftware")
    @ApiOperation(value = "修改软件环境需求", notes = "")
        @PreAuthorize("hasAuthority('/projectDoc/editEnvSoftware')")
    public boolean editEnvSoftware(@Valid @RequestBody EnvSoftware envSoftware) {
        return envSoftwareService.updateById(envSoftware);
    }

    @PostMapping("/EnvSoftwareList")
    @ApiOperation(value = "查询软件环境需求", notes = "")
    @PreAuthorize("hasAuthority('/projectDoc/EnvSoftwareList')")
    public List<EnvSoftware> envSoftwareList() {
        return envSoftwareService.list();
    }

    @PostMapping("/deleteEnvSoftware")
    @ApiOperation(value = "删除软件环境需求", notes = "只要idList里id存在的删，不存在的不删，全部不存在返回false")
    @PreAuthorize("hasAuthority('/projectDoc/deleteEnvSoftware')")
    public boolean deleteEnvSoftware(@NotNull(message = "idList不能为空") @RequestParam List<Integer> idList) {
        QueryWrapper<EnvSoftware> queryWrapper = new QueryWrapper<>();
        queryWrapper.in("id", idList);
        return envSoftwareService.remove(queryWrapper);
    }


}
