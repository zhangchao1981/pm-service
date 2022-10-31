package com.iscas.pm.api.service.impl.doc;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.iscas.pm.api.mapper.doc.DocumentMapper;
import com.iscas.pm.api.mapper.env.EnvHardwareMapper;
import com.iscas.pm.api.mapper.env.EnvSoftwareMapper;
import com.iscas.pm.api.model.dev.*;
import com.iscas.pm.api.model.doc.*;
import com.iscas.pm.api.model.doc.data.DocDBTableTemp;
import com.iscas.pm.api.model.doc.data.DocInterface;
import com.iscas.pm.api.model.doc.data.DocPlanTask;
import com.iscas.pm.api.model.doc.data.DocReviseRecord;
import com.iscas.pm.api.model.doc.param.CreateDocumentParam;
import com.iscas.pm.api.model.doc.data.DocModular;
import com.iscas.pm.api.model.doc.data.DocRequirement;
import com.iscas.pm.api.model.env.EnvHardware;
import com.iscas.pm.api.model.env.EnvSoftware;
import com.iscas.pm.api.model.project.ProjectMember;
import com.iscas.pm.api.model.projectPlan.PlanTask;
import com.iscas.pm.api.service.*;
import com.iscas.pm.api.util.DocumentHandler;
import com.iscas.pm.api.util.FastDFSUtil;
import com.iscas.pm.api.util.WordUtil;
import com.iscas.pm.common.core.util.RedisUtil;
import com.iscas.pm.common.core.util.TreeUtil;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import com.iscas.pm.common.db.separate.datasource.DynamicDataSource;
import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import javax.servlet.http.HttpServletResponse;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author 66410
 * @description 针对表【document】的数据库操作Service实现
 * @createDate 2022-07-28 18:20:53
 */
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService {
    @Autowired
    DocumentMapper documentMapper;
    @Autowired
    FastDFSUtil fastDFSUtil;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    private FastFileStorageClient fastFileStorageClient;
    @Autowired
    private ProjectInfoService projectInfoService;
    @Autowired
    DocumentHandler documentHandler;
    @Autowired
    EnvHardwareMapper hardwareMapper;
    @Autowired
    EnvSoftwareMapper softwareMapper;
    @Autowired
    ProjectPlanService projectPlanService;
    @Autowired
    ReviseRecordService reviseRecordService;
    @Autowired
    ReferenceDocService referenceDocService;
    @Autowired
    ProjectTeamService projectTeamService;
    @Autowired
    DocTemplateService docTemplateService;
    @Autowired
    DynamicDataSource dynamicDataSource;
    @Autowired
    DevRequirementService devRequirementService;
    @Autowired
    DevModularService devModularService;
    @Autowired
    DevInterfaceService devInterfaceService;
    @Autowired
    DataRequirementService dataRequirementService;


    @Override
    public Document addLocalDocument(Document document) {
        documentChecking(document);
        document.setPath(document.getPath());
        documentMapper.insert(document);
        return document;
    }

    private void documentChecking(Document document) {
        if (getDocumentByDirectoryId(document.getDirectoryId()) == null)
            throw new IllegalArgumentException("所属目录不存在");
        if (existSameNameDoc(document.getDirectoryId(), document.getName(), "add"))
            throw new IllegalArgumentException("该目录下已存在同名文档");
    }

    @Override
    public Boolean deleteDocument(Integer id) {
        Document document = documentMapper.selectById(id);
        if (document == null)
            throw new IllegalArgumentException("待删除的文档不存在");
        if (StringUtils.isNotBlank(document.getPath()) && document.getType() != DocumentTypeEnum.LINK)
            fastDFSUtil.delete(document.getPath());
        return true;
    }

    @Override
    public Boolean deleteDocumentBatch(List<Integer> ids) {
        ids.forEach(id -> {
            deleteDocument(id);
        });
        return null;
    }

    @Override
    public void downloadDocument(Integer id, HttpServletResponse response) {
        Document document = documentMapper.selectById(id);
        if (document == null)
            throw new IllegalArgumentException("待下载的文档不存在");

        if (StringUtils.isNotBlank(document.getPath()) && document.getType() != DocumentTypeEnum.LINK) {
            try {
                fastDFSUtil.download(document.getPath(), document.getName(), response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    @Override
    public void createDocument(CreateDocumentParam createDocumentParam) throws IOException {
        String currentProject = DataSourceHolder.getDB().databaseName;
        Document autoDoc = new Document();
        autoDoc.setDirectoryId(createDocumentParam.getDirectoryId());
        autoDoc.setUploader(RequestHolder.getUserInfo().getEmployeeName());
        autoDoc.setName(createDocumentParam.getDocumentName());
        autoDoc.setType(DocumentTypeEnum.GENERATE);
        autoDoc.setVersion(createDocumentParam.getVersion());
        Integer templateId = createDocumentParam.getTemplateId();
        documentChecking(autoDoc);

        //拿到服务器中模板的存储路径
        StorePath storePath = StorePath.parseFromUrl(createDocumentParam.getTemplatePath());
        byte[] sourceByte = fastFileStorageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
        if (null == sourceByte) {
            throw new IllegalArgumentException("模板路径错误，服务器读取不到该文件");
        }
        String fileName = createDocumentParam.getDocumentName();
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        InputStream template = new ByteArrayInputStream(sourceByte);

        //前端传入参数只含共用的信息，数据库查找的信息根据模板类型选择性填充
        HashMap<String, Object> map = getDocumentContext(createDocumentParam);

        //用户输入内容：
        map.put("本文档版本号", createDocumentParam.getVersion());
        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        ConfigureBuilder builder = Configure.builder();
        map.keySet().forEach(key -> {
            if (key.endsWith("List")) {
                builder.bind(key, policy);

                //不以list结尾的特殊列表,单独进行关联
                if (key.startsWith("modularList")) {
                    //软需列表
                    builder.bind("modulars", policy)
                            .bind("precondition", policy)
                            .bind("successScene", policy)
                            .bind("branchScene", policy)
                            .bind("constraint", policy)
                            .bind("dataDescription", policy)
                            .bind("dataInfo",policy);
                }
            } else if (key.startsWith("docDBTableTemps")) {
                //数据库列表 :
                // session底下的DocDBTableTemp(tableName=dev_interface, tableStructureList=
                builder.bind("tableStructureList", policy);
            }
        });
        Configure config = builder.build();
        //可优化
        WordUtil.parse(template, map, out, config);
        //将输出文件上传到服务器：
        //输出文件的比特流
        byte[] bytes = out.toByteArray();
        //拿到文件内容，输出到fastDFS服务器上
        InputStream inputStream = new ByteArrayInputStream(bytes);
        String upLoadPath = fastDFSUtil.uploadByIO(inputStream, fileName + ".doc").getFullPath();
        //生成的文档上传到fastDfs  返回存储路径存储到mysql里（存储到document表里）
        autoDoc.setPath(upLoadPath);
        autoDoc.setUpdateTime(new Date());
        autoDoc.setCreateTime(new Date());
        //切回当前项目
        DataSourceHolder.setDB(currentProject);
        if (addLocalDocument(autoDoc) == null) {
            throw new IOException("自动上传文档失败");
        }
    }


    @Override
    public HashMap<String, Object> getDocumentContext(CreateDocumentParam createDocumentParam) {
        Integer templateId = createDocumentParam.getTemplateId();
        HashMap<String, Object> map = new HashMap<>();
        String currentProject = DataSourceHolder.getDB().databaseName;

        //填充通用内容：
        List<ReviseRecord> reviseRecordList = reviseRecordService.list(new QueryWrapper<ReviseRecord>().eq("template_id", templateId));
        List<ReferenceDoc> referenceList = referenceDocService.list(new QueryWrapper<ReferenceDoc>().eq("template_id", templateId));
        List<DocReviseRecord> docReviseRecordList = new ArrayList<>();
        reviseRecordList.forEach(reviseRecord -> {
            docReviseRecordList.add(new DocReviseRecord(reviseRecord));
        });
        map.put("reviseRecordList", docReviseRecordList);
        map.put("referenceList", referenceList);
        DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
        ProjectDetailInfo projectDetailInfo = projectInfoService.getProjectDetailInfo(currentProject);
        map.put("项目名称", projectDetailInfo.getBasicInfo().getName());
        map.put("项目标识", projectDetailInfo.getBasicInfo().getId());
        map.put("项目阶段", projectDetailInfo.getBasicInfo().getStatus());
        map.put("需求提出方", projectDetailInfo.getBasicInfo().getRequirementProvider());
        map.put("项目密级", projectDetailInfo.getBasicInfo().getSecretLevel().getValue());
        map.put("研制单位", projectDetailInfo.getBasicInfo().getManufacture());
        map.put("项目提出方", projectDetailInfo.getBasicInfo().getProjectProvider());
        //获取模板类型
        TemplateTypeEnum templateType = Assert.notNull(docTemplateService.getById(createDocumentParam.getTemplateId()), "所选模板不存在").getType();

        //根据模板id填充特定内容:
        switch (templateType) {
            default:
                break;
//                throw new IllegalArgumentException("未查询到该模板对应数据");
            case SoftwareRequirementsSpecification: {
                DataSourceHolder.setDB(currentProject);

                //所有modular (树结构)
                List<DevModular> modularList = devModularService.list();
                List<DocModular> docModularList = new ArrayList<>();

                //根据开发需求查询:所有含开发需求的modular的List集合, devRequirement替换为docRequirement
                List<DevRequirement> devRequirementList = devRequirementService.list();
                List<DocRequirement> docRequirementList = new ArrayList<>();
                devRequirementList.forEach(devRequirement -> {
                    docRequirementList.add(new DocRequirement(devRequirement).setProjectId(map.get("项目标识").toString()));
                });
                Map<Integer, List<DocRequirement>> requirementMap = docRequirementList.stream().collect(Collectors.groupingBy(DocRequirement::getModularId));

                //性能需求
                List<DocRequirement> performanceReqList = docRequirementList.stream().filter(docRequirementRequirement -> docRequirementRequirement.getRequirementType().equals(RequirementTypeEnum.PERFORMANCE)).collect(Collectors.toList());
                //用DocModular 将modular 中的开发需求属性填充上
                modularList.forEach(modular -> {
                    docModularList.add(new DocModular(modular).setProjectId(map.get("项目标识").toString()));
                });
                docModularList.forEach(docModular -> {
                            if (requirementMap.containsKey(docModular.getId())) {
                                docModular.setDocRequirements(requirementMap.get(docModular.getId()));
                            }
                        }
                );
                List<DocModular> modularTreeList = TreeUtil.treeOut(docModularList, DocModular::getId, DocModular::getParentId, DocModular::getModulars);

                //如果用hashMap  需要new hash -->key是session  value是hash  这个hash是一个list集合  集合对象包含externalInterface和项目标识   优点是不需要新实体类,缺点是需要把原实体类属性和对象都拉出来放到hash里
                //如果用新建实体类  需要加属性和构造器   此处采用方案2
                List<DocInterface> externalInterfaceList =new ArrayList<>();
                List<DevInterface> devInterfaces = devInterfaceService.devInterfaceListByType(InterfaceTypeEnum.EXTERNAL_INTERFACE.getCode());
                if (devInterfaces.size()>0){
                    devInterfaces.forEach(devInterface->externalInterfaceList.add(new DocInterface(devInterface,map.get("项目标识").toString())));
                }
                List<DocInterface> internalInterfaceList =new ArrayList<>();
                List<DevInterface> devInterfaces2 = devInterfaceService.devInterfaceListByType(InterfaceTypeEnum.INTERNAL_INTERFACE.getCode());
                if (devInterfaces2.size()>0){
                    devInterfaces2.forEach(devInterface->internalInterfaceList.add(new DocInterface(devInterface,map.get("项目标识").toString())));
                }
                List<DataRequirement> dataRequirementList = dataRequirementService.list();
                List<EnvHardware> hardwareList = hardwareMapper.selectList(new QueryWrapper<>());
                List<EnvSoftware> softwareList = softwareMapper.selectList(new QueryWrapper<>());

                map.put("modularList", modularTreeList);
                map.put("performanceReqList", performanceReqList);
                map.put("externalInterfaceList",externalInterfaceList);
                map.put("internalInterfaceList",internalInterfaceList);
                map.put("dataRequirementList",dataRequirementList);
                map.put("hardwareList", hardwareList);
                map.put("softwareList", softwareList);
                map.put("docRequirementList",docRequirementList);
                break;
            }

            case SoftwareDevelopment: {
                DataSourceHolder.setDB(currentProject);
                List<EnvHardware> hardwareList = hardwareMapper.selectList(new QueryWrapper<>());
                List<EnvSoftware> softwareList = softwareMapper.selectList(new QueryWrapper<>());

                //项目计划信息获取
                List<PlanTask> planTaskList = projectPlanService.getTaskListByWbs();
                List<ProjectMember> memberList = projectTeamService.memberRoleList();
                List<DocPlanTask> docPlanTaskList = new ArrayList<>();
                planTaskList.forEach(planTask -> {
                    docPlanTaskList.add(new DocPlanTask(planTask));
                });
                map.put("memberList", memberList);
                map.put("hardwareList", hardwareList);
                map.put("softwareList", softwareList);
                map.put("planTaskList", docPlanTaskList);
                break;
            }
            case DatabaseDesignNotes: {
                if (createDocumentParam.getDbType() == DataBaseTypeEnum.MYSQL) {
                    List<DocDBTableTemp> docDBTableTempList = new ArrayList<>();
                    //连接自定义数据库
                    String dataSourceName = UUID.randomUUID().toString();
                    String url, driverName;
                    if (createDocumentParam.getDbType() == DataBaseTypeEnum.MYSQL) {
                        url = "jdbc:mysql://" + createDocumentParam.getDbPath() + ":" + createDocumentParam.getPort() + "/" + createDocumentParam.getDbName() + "?useUnicode=true&useSSL=false&characterEncoding=utf8&serverTimezone=UTC&allowPublicKeyRetrieval=true";
                        driverName = "com.mysql.cj.jdbc.Driver";
                    } else if (createDocumentParam.getDbType() == DataBaseTypeEnum.ORACLE) {
                        url = "jdbc:oracle:thin:@" + createDocumentParam.getDbPath() + ":" + createDocumentParam.getPort() + ":" + createDocumentParam.getDbName();
                        driverName = "oracle.jdbc.driver.OracleDriver";
                    } else {
                        throw new IllegalArgumentException("暂不支持该数据库类型！");
                    }
                    DataSourceHolder.setDB(url, createDocumentParam.getDbName(), createDocumentParam.getUserName(), createDocumentParam.getPassword(), driverName, dataSourceName);
                    try {
                        getDBInfo(createDocumentParam.getDbName());
                    } catch (Exception e) {
                        throw new IllegalArgumentException("数据库连接失败，请重新确认参数是否正确");
                    } finally {
                        dynamicDataSource.deleteDataSourceByName(dataSourceName);
                    }

                    //数据库中对应的所有表的表名()
                    List<TableByDB> tableList = getDBInfo(createDocumentParam.getDbName());
                    tableList.forEach(table -> {
                        //表结构数据处理
                        List<TableStructure> tableStructureList = getTableStructureList(table.getName());
                        tableStructureList.stream().forEach(tableStructure -> {
                            // key=MUL则为外键
                            //key=PRI 则为主键
                            if (StringUtils.isNotBlank(tableStructure.key)) {
                                if ("PRI".equals(tableStructure.key)) {
                                    tableStructure.setKey("是");
                                }
                                if ("MUL".equals(tableStructure.key)) {
                                    tableStructure.setKey("否");
                                    tableStructure.setExtra("是");
                                } else {
                                    tableStructure.setExtra("否");
                                }
                            } else {
                                tableStructure.setKey("否");
                                tableStructure.setExtra("否");
                            }
                            tableStructure.setNull("YES".equals(tableStructure.Null) ? "是" : "否");
                        });
                        DocDBTableTemp tableTemp = new DocDBTableTemp().setTableName(table.name).setTableComment(table.comment).setTableStructureList(tableStructureList);
                        docDBTableTempList.add(tableTemp);
                    });
                    //新建一个表实体类   对应表格头   表格体内的变量    首先有String :tableHead  有List集合放的表数据 List<TableStructure>
                    //把表格实体类封装成 List集合
                    map.put("docDBTableTemps", docDBTableTempList);
                    break;
                }
            }
        }
        return map;
    }

    @Override
    public void deleteTemplate(Integer templateId) {
        Document document = documentMapper.selectById(templateId);
        Assert.notNull(document, "要删除的文档不存在");
        if (!StringUtils.isEmpty(document.getPath())) {
            fastDFSUtil.delete(document.getPath());
        }
        documentMapper.deleteById(templateId);
    }

    @Override
    public List<TableByDB> getDBInfo(String dbName) {
        return documentMapper.getDBInfo(dbName);
    }

    @Override
    public List<TableStructure> getTableStructureList(String tableName) {
        return documentMapper.getTableStructureList(tableName);
    }

    /**
     * 校验目录是否存在
     */
    private List<Document> getDocumentByDirectoryId(Integer directoryId) {
        return documentMapper.selectList(new QueryWrapper<Document>().eq(null != directoryId, "directory_id", directoryId));
    }

    /**
     * 校验同一目录下是否有同名文档
     */
    private Boolean existSameNameDoc(Integer directoryId, String name, String ops) {
        QueryWrapper<Document> documentQueryWrapper = new QueryWrapper<>();
        if ("add".equals(ops)) {
            documentQueryWrapper.eq(null != directoryId, "directory_id", directoryId);
        } else {
            documentQueryWrapper.ne(null != directoryId, "directory_id", directoryId);
        }
        documentQueryWrapper.eq(null != name, "name", name);
        List<Document> documents = documentMapper.selectList(documentQueryWrapper);
        return documents != null && documents.size() != 0;
    }
}




