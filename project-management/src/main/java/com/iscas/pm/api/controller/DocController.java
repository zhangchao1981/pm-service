package com.iscas.pm.api.controller;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.iscas.pm.api.model.doc.*;
import com.iscas.pm.api.model.doc.param.CreateDocumentParam;
import com.iscas.pm.api.model.doc.param.DBLinkParam;
import com.iscas.pm.api.model.doc.param.DocumentQueryParam;
import com.iscas.pm.api.service.*;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import com.iscas.pm.common.db.separate.config.DatasourceFactory;
import com.iscas.pm.common.db.separate.datasource.DynamicDataSource;
import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
import io.swagger.annotations.*;
import org.apache.commons.lang3.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;
import javax.validation.constraints.NotEmpty;
import javax.validation.constraints.NotNull;
import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.UUID;

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
    @Autowired
    DatasourceFactory datasourceFactory;
    @Autowired
    DynamicDataSource dynamicDataSource;

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
    public IPage<Document> getDocumentBatch(@RequestBody @Valid DocumentQueryParam documentQueryParam) {
        IPage<Document> documentIPage = documentService.page(new Page<>(documentQueryParam.getPageNum(), documentQueryParam.getPageSize()), new QueryWrapper<Document>()
                .eq(documentQueryParam.getDirectoryId() != null, "directory_id", documentQueryParam.getDirectoryId()).like(!StringUtils.isBlank(documentQueryParam.getDocName()), "name", documentQueryParam.getDocName()));
        return documentIPage;
    }

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
        return documentService.deleteDocument(documentId);
    }

    @PostMapping("/deleteDocumentBatch")
    @ApiOperation(value = "批量删除文档", notes = "参数不可为空,删除参数list中所有id对应的文档")
    @ApiOperationSupport(order = 14)
    @PreAuthorize("hasAuthority('/projectDoc/deleteDocumentBatch')")
    public boolean deleteDocumentBatch(@NotEmpty(message = "参数Id列表不能为空") @RequestBody List<Integer> docIdList) {
        return documentService.deleteDocumentBatch(docIdList);
    }

    @PostMapping("/addReferenceDoc")
    @ApiOperation(value = "添加引用文档", notes = "templateId不存在则抛出 不符合数据库约束性，导致异常 ")
    @ApiOperationSupport(order = 16)
    @PreAuthorize("hasAuthority('/projectDoc/addReferenceDoc')")
    public Boolean addReferenceDoc(@Valid @RequestBody ReferenceDoc referenceDoc) {
        checkDocTemplate(referenceDoc.getTemplateId());
        return referenceDocService.save(referenceDoc);
    }

    @PostMapping("/editReferenceDoc")
    @ApiOperation(value = "修改引用文档")
    @ApiOperationSupport(order = 17)
    @PreAuthorize("hasAuthority('/projectDoc/editReferenceDoc')")
    public boolean editReferenceDoc(@Valid @RequestBody ReferenceDoc referenceDoc) {
        checkDocTemplate(referenceDoc.getTemplateId());
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
        checkDocTemplate(reviseRecord.getTemplateId());
        return reviseRecordService.save(reviseRecord);
    }

    @PostMapping("/editReviseRecord")
    @ApiOperation(value = "修改修订记录", notes = "")
    @ApiOperationSupport(order = 21)
    @PreAuthorize("hasAuthority('/projectDoc/editReviseRecord')")
    public boolean editReviseRecord(@Valid @RequestBody ReviseRecord reviseRecord) {
        checkDocTemplate(reviseRecord.getTemplateId());
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

    private void checkDocTemplate(Integer templateId) {
        String currentProject = DataSourceHolder.getDB().databaseName;
        DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
        if (docTemplateService.getById(templateId) == null) {
            throw new IllegalArgumentException("templateId对应模板不存在");
        }
        DataSourceHolder.setDB(currentProject);
    }

    @PostMapping("/deleteTemplate")
    @ApiOperation(value = "删除文档模板", notes = "待开发")
    @ApiOperationSupport(order = 24)
    @PreAuthorize("hasAuthority('/projectDoc/deleteTemplate')")
    public void deleteTemplate(@NotNull(message = "模板id不能为空") Integer templateId) {
        DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
        //是否要校验   receive_recode/ reference有记录   (目前是外键级联删)
        documentService.deleteTemplate(templateId);
    }

    @PostMapping("/addTemplate")
    @ApiOperation(value = "添加文档模板", notes = "上传本地文档模板到服务器")
    @ApiOperationSupport(order = 25)
    @PreAuthorize("hasAuthority('/projectDoc/addTemplate')")
    public DocTemplate addTemplate(@Valid @RequestBody DocTemplate docTemplate) throws IOException {
        DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
        return docTemplateService.uploadLocalTemplate(docTemplate);
    }

    @PostMapping("/editTemplate")
    @ApiOperation(value = "修改文档模板", notes = "")
    @ApiOperationSupport(order = 26)
    @PreAuthorize("hasAuthority('/projectDoc/editTemplate')")
    public boolean editTemplate(@Valid @RequestBody DocTemplate template) {
        DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
        DocTemplate oldTemplate = docTemplateService.getById(template.getId());
        if (oldTemplate==null){
            throw new IllegalArgumentException("要修改的template不存在");
        }
        template.setCreateTime(oldTemplate.getCreateTime());
        template.setUpdateTime(new Date());
        if (!docTemplateService.save(template)) {
            throw new IllegalArgumentException("要修改的template不存在");
        }
        //待添加校验( 存储路径是否改变 改变则删除服务器上的旧文档模板)
        return true;
    }

    @GetMapping("/TemplateList")
    @ApiOperation(value = "查询文档模板", notes = "不带分页，用于生成文档时选择模板")
    @ApiOperationSupport(order = 27)
    @PreAuthorize("hasAuthority('/projectDoc/templateList')")
    public List<DocTemplate> templateList() {
        DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
        return docTemplateService.list();
    }

    @GetMapping("/templatePageList")
    @ApiOperation(value = "分页查询文档模板", notes = "分页，用于文档模板管理界面显示,参数是当前页、每页显示记录条数")
    @ApiOperationSupport(order = 28)
    @PreAuthorize("hasAuthority('/projectDoc/templatePageList')")
    public IPage<DocTemplate> templatePageList(@RequestParam Integer pageNum, @RequestParam Integer pageSize) {
        DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
        return docTemplateService.page(new Page<>(pageNum, pageSize));
    }

    @PostMapping("/testDB")
    @ApiOperation(value = "测试数据库连接", notes = "测试数据库能否正常连接")
    @ApiOperationSupport(order = 29)
    public Boolean testDB(@RequestBody @Valid DBLinkParam dbLinkParam) {
        String dataSourceName = UUID.randomUUID().toString();
        String url, driverName;
        if (dbLinkParam.getDbType() == DataBaseTypeEnum.MYSQL) {
            url = "jdbc:mysql://" + dbLinkParam.getDbPath() + ":" + dbLinkParam.getPort() + "/" + dbLinkParam.getDbName() + "?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC&allowPublicKeyRetrieval=true";
            driverName = "com.mysql.cj.jdbc.Driver";
        } else if (dbLinkParam.getDbType() == DataBaseTypeEnum.ORACLE) {
            url = "jdbc:oracle:thin:@" + dbLinkParam.getDbPath() + ":" + dbLinkParam.getPort() + ":" + dbLinkParam.getDbName();
            driverName = "oracle.jdbc.driver.OracleDriver";
        } else {
            throw new IllegalArgumentException("暂不支持该数据库类型！");
        }
        DataSourceHolder.setDB(url, dbLinkParam.getDbName(), dbLinkParam.getUserName(), dbLinkParam.getPassword(), driverName, dataSourceName);
        try {
            documentService.getDBInfo(dbLinkParam.getDbName());
        } catch (Exception e) {
           throw new IllegalArgumentException("数据库连接失败，请重新确认参数是否正确");
        } finally {
            dynamicDataSource.deleteDataSourceByName(dataSourceName);
        }
        return true;
    }


}