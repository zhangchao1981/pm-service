package com.iscas.pm.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iscas.pm.api.model.doc.*;
import com.iscas.pm.api.model.doc.param.AddTemplateParam;
import com.iscas.pm.api.model.doc.param.CreateDocumentParam;
import com.iscas.pm.api.model.doc.param.DocumentQueryParam;
import com.iscas.pm.api.service.*;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
    @Autowired
    HttpServletResponse response;


    @GetMapping("/findDirectory")
    @ApiOperation(value = "查询目录树", notes = "查询整棵目录树")
    @ApiOperationSupport(order = 1)
    @PreAuthorize("hasAuthority('/projectDoc/findDirectory')")
    public List<Directory> getAll() {
        return directoryService.getDirectoryTree();
    }


    @PostMapping("/addDirectory")
    @ApiOperation(value = "添加目录", notes = "id自动生成，前端不用传,children属性是查询显示的，添加不传该值")
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
        if (!directoryService.removeById(id)) {
            throw new IllegalArgumentException("id对应目录不存在");
        }
        return true;
    }


    @PostMapping("/editDirectory")
    @ApiOperation(value = "修改目录", notes = "修改目录名称(或父id)， children属性是查询显示的，修改不传该值")
    @ApiOperationSupport(order = 4)
    @PreAuthorize("hasAuthority('/projectDoc/editDirectory')")
    public Directory editDirectory(@Valid @RequestBody Directory directory) {
        return directoryService.editDirectory(directory);
    }


    @PostMapping("/getDocumentBatch")
    @ApiOperation(value = "查询文档", notes = "根据指定文档目录或文档名查询对应文档,没有文档名时按目录查，有文档名时查询所在目录下的文档(根目录下查询所有文档)")
    @ApiOperationSupport(order = 14)





    @PreAuthorize("hasAuthority('/projectDoc/getDocumentBatch')")
    public IPage<Document> getDocumentBatch(@RequestBody @Valid  DocumentQueryParam documentQueryParam) {
        Integer directoryId = documentQueryParam.getDirectoryId();
        String docName = documentQueryParam.getDocName();
        IPage<Document> documentIPage = documentService.page(new Page<>(documentQueryParam.getPageNum(), documentQueryParam.getDirectoryId()), new QueryWrapper<Document>()
                                 .eq(directoryId != null, "directory_id", directoryId).eq(!StringUtils.isBlank(docName), "name", docName));
        return    documentIPage;
    }

//    @PostMapping("/findDocumentByName")
//    @ApiOperation(value = "查询文档", notes = "根据文档名模糊查询对应文档")
//    @ApiOperationSupport(order = 14)
//    @PreAuthorize("hasAuthority('/projectDoc/findDocumentByName')")
//    public List<Document> findDocumentByName(@NotBlank(message = "参数Id列表不能为空") @RequestParam String docName) {
//        return documentService.list(new QueryWrapper<Document>().like("name", docName));
//    }


    @PostMapping("/createDocument")
    @ApiOperation(value = "文档生成", notes = "选择指定模板，自动生成对应文档并上传到服务器上,并在数据库记录文档信息")
    @ApiOperationSupport(order = 11)
    @PreAuthorize("hasAuthority('/projectDoc/createDocument')")
    public void createDocument(@RequestBody CreateDocumentParam createDocumentParam) throws IOException {
        documentService.createDocument(createDocumentParam);
    }

    @PostMapping("/addLocalDocument")
    @ApiOperation(value = "添加本地文档", notes = "将文档信息存储到mysql(包括文档在服务器上的存储路径)")
    @ApiOperationSupport(order = 11)
    @PreAuthorize("hasAuthority('/projectDoc/addLocalDocument')")
    public Document addLocalDocument(@Valid @RequestBody Document document) {
        if (StringUtils.isBlank(document.getPath())) {
            throw new IllegalArgumentException("文档路径不能为空");
        }
        document.setCreateTime(new Date());
        document.setUpdateTime(new Date());
        document.setUploader(RequestHolder.getUserInfo().getEmployeeName());
        documentService.addLocalDocument(document);
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
    public boolean deleteBatchDocument(@NotEmpty(message = "参数Id列表不能为空") @RequestBody List<Integer> docIdList) {
        if (!documentService.remove(new QueryWrapper<Document>().in("id", docIdList))) {
            throw new IllegalArgumentException("删除失败，文档不存在");
        }
        return true;
    }



    @PostMapping("/addReferenceDoc")
    @ApiOperation(value = "添加引用文档", notes = "templateId不存在则抛出 不符合数据库约束性，导致异常 ")
    @ApiOperationSupport(order = 16)
    @PreAuthorize("hasAuthority('/projectDoc/addReferenceDoc')")
    public Boolean addReferenceDoc(@Valid @RequestBody ReferenceDoc referenceDoc) {
        if (documentService.getById(referenceDoc.getTemplateId()) == null) {
            throw new IllegalArgumentException("引用文档templateId对应模板不存在");
        }
        return referenceDocService.save(referenceDoc);
    }

    @PostMapping("/editReferenceDoc")
    @ApiOperation(value = "修改引用文档")
    @ApiOperationSupport(order = 17)
    @PreAuthorize("hasAuthority('/projectDoc/editReferenceDoc')")
    public boolean editReferenceDoc(@Valid @RequestBody ReferenceDoc referenceDoc) {
        if (documentService.getById(referenceDoc.getTemplateId()) == null) {
            throw new IllegalArgumentException("引用文档templateId对应模板不存在");
        }
        return referenceDocService.updateById(referenceDoc);
    }



    @PostMapping("/referenceDocList")
    @ApiOperation(value = "查询引用文档", notes = "")
    @ApiOperationSupport(order = 18)
    @PreAuthorize("hasAuthority('/projectDoc/referenceDocList')")
    public List<ReferenceDoc> referenceDocList(@NotNull(message = "引用文档Id不能为空") @RequestParam Integer templateId) {
        return referenceDocService.list(new QueryWrapper<ReferenceDoc>().eq("template_id", templateId));
    }




    @PostMapping("/deleteReferenceDoc")
    @ApiOperation(value = "删除引用文档", notes = "")
    @ApiOperationSupport(order = 19)
    @PreAuthorize("hasAuthority('/projectDoc/deleteReferenceDoc')")
    public boolean deleteReferenceDoc(@NotEmpty(message = "id不能为空") @RequestParam Integer referenceId) {
        if (!referenceDocService.remove(new QueryWrapper<ReferenceDoc>().eq("id", referenceId))) {
            throw new IllegalArgumentException("要删除的引用文档不存在");
        }
        return true;
    }

    @PostMapping("/addReviseRecord")
    @ApiOperation(value = "添加修订记录", notes = "")
    @ApiOperationSupport(order = 20)
    @PreAuthorize("hasAuthority('/projectDoc/addReviseRecord')")
    public Boolean addReviseRecord(@Valid @RequestBody ReviseRecord reviseRecord) {
        if (documentService.getById(reviseRecord.getTemplateId()) == null) {
            throw new IllegalArgumentException("引用文档templateId对应模板不存在");
        }
        return reviseRecordService.save(reviseRecord);
    }

    @PostMapping("/editReviseRecord")
    @ApiOperation(value = "修改修订记录", notes = "")
    @ApiOperationSupport(order = 21)
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
    @ApiOperationSupport(order = 22)
    @PreAuthorize("hasAuthority('/projectDoc/ReviseRecordList')")
    public List<ReviseRecord> reviseRecordList(@NotNull(message = "templateId不能为空") @RequestParam Integer templateId) {
        return reviseRecordService.list(new QueryWrapper<ReviseRecord>().eq("template_id", templateId));
    }

    @PostMapping("/deleteReviseRecord")
    @ApiOperation(value = "删除修订记录")
    @ApiOperationSupport(order = 23)
    @PreAuthorize("hasAuthority('/projectDoc/deleteReviseRecord')")
    public boolean deleteReviseRecord(@NotNull(message = "id不能为空") @RequestParam Integer reviseRecordId) {
        if (!reviseRecordService.remove(new QueryWrapper<ReviseRecord>().eq("id", reviseRecordId))) {
            throw new IllegalArgumentException("要删除的修订记录不存在");
        }
        return true;
    }

    //是否要校验   receive_recode/ reference有记录   (目前是外键级联删)
    @PostMapping("/deleteTemplate")
    @ApiOperation(value = "删除文档模板", notes = "待开发")
    @ApiOperationSupport(order = 24)
    @PreAuthorize("hasAuthority('/projectDoc/deleteTemplate')")
    public boolean deleteTemplate(@NotEmpty(message = "模板id不能为空") @RequestParam List<Integer> idList) {
        if (!docTemplateService.removeByIds(idList)) {
            throw new IllegalArgumentException("要删除的模板id不存在");
        }
        return true;
    }


    @PostMapping("/addTemplate")
    @ApiOperation(value = "添加文档模板", notes = "上传本地文档模板到数据库")
    @ApiOperationSupport(order = 25)
    @PreAuthorize("hasAuthority('/projectDoc/addTemplate')")
    public DocTemplate addTemplate(@Valid @RequestBody AddTemplateParam addTemplateParam) throws IOException {
        return docTemplateService.addLocalDocument(addTemplateParam);
    }



    @PostMapping("/editTemplate")
    @ApiOperation(value = "修改文档模板", notes = "")
    @ApiOperationSupport(order = 28)
    @PreAuthorize("hasAuthority('/projectDoc/editTemplate')")
    public boolean editTemplate(@Valid @RequestBody DocTemplate template) {
        if (!docTemplateService.save(template)) {
            throw new IllegalArgumentException("要修改的template不存在");
        }
        return true;
    }

    @PostMapping("/TemplateList")
    @ApiOperation(value = "查询文档模板", notes = "")
    @ApiOperationSupport(order = 29)
    @PreAuthorize("hasAuthority('/projectDoc/templateList')")
    public List<DocTemplate> templateList(@NotBlank(message = "templateId不能为空") @RequestParam String nameOrMaintainer) {
        return docTemplateService.list(new QueryWrapper<DocTemplate>().like("name", nameOrMaintainer).or().like("maintainer", nameOrMaintainer));
    }


}