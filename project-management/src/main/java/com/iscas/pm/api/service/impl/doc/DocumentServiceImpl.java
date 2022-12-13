package com.iscas.pm.api.service.impl.doc;

import cn.hutool.core.lang.Assert;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.toolkit.BeanUtils;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.config.ConfigureBuilder;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.iscas.pm.api.mapper.doc.DocumentMapper;
import com.iscas.pm.api.mapper.env.EnvHardwareMapper;
import com.iscas.pm.api.mapper.env.EnvSoftwareMapper;
import com.iscas.pm.api.model.dev.*;
import com.iscas.pm.api.model.doc.*;
import com.iscas.pm.api.model.doc.data.*;
import com.iscas.pm.api.model.doc.param.CreateDocumentParam;
import com.iscas.pm.api.model.doc.param.DBLinkParam;
import com.iscas.pm.api.model.env.EnvHardware;
import com.iscas.pm.api.model.env.EnvSoftware;
import com.iscas.pm.api.model.project.Project;
import com.iscas.pm.api.model.project.ProjectMember;
import com.iscas.pm.api.model.projectPlan.PlanTask;
import com.iscas.pm.api.model.test.BugStatistics;
import com.iscas.pm.api.model.test.TestPlan;
import com.iscas.pm.api.model.test.TestUseCase;
import com.iscas.pm.api.model.test.enums.UseCaseTypeEnum;
import com.iscas.pm.api.service.*;
import com.iscas.pm.api.util.DateUtil;
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
import java.io.*;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.stream.Collectors;


/**
 * @author lichang
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
    FastFileStorageClient fastFileStorageClient;
    @Autowired
    ProjectInfoService projectInfoService;
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
    @Autowired
    TestPlanService testPlanService;
    @Autowired
    TestBugService testBugService ;
    @Autowired
    TestUseCaseService testUseCaseService;

    @Override
    public Document addLocalDocument(Document document) {
        documentChecking(document);
        document.setPath(document.getPath());
        documentMapper.insert(document);
        return document;
    }

    @Override
    public Boolean deleteDocument(Integer id) {
        Document document = documentMapper.selectById(id);
        if (document == null)
            throw new IllegalArgumentException("待删除的文档不存在");

        try {
            if (StringUtils.isNotBlank(document.getPath()) && document.getType() != DocumentTypeEnum.LINK)
                fastDFSUtil.delete(document.getPath());
        } catch (RuntimeException e) {
            e.printStackTrace();
        } finally {
            documentMapper.deleteById(id);
        }
        return true;
    }

    @Override
    public Boolean deleteDocumentBatch(List<Integer> ids) {
        ids.forEach(id -> {
            deleteDocument(id);
        });
        return true;
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
                throw new RuntimeException("该文档存储路径已失效");
            }
        }
    }

    @Override
    public void createDocument(CreateDocumentParam createDocumentParam) throws IOException {
        String currentProject = DataSourceHolder.getDB().databaseName;

        Document document = new Document();
        document.setDirectoryId(createDocumentParam.getDirectoryId());
        document.setUploader(RequestHolder.getUserInfo().getEmployeeName());
        document.setName(createDocumentParam.getDocumentName());
        document.setType(DocumentTypeEnum.GENERATE);
        document.setVersion(createDocumentParam.getVersion());
        documentChecking(document);

        //从FastDFS服务器获取模板文件
        StorePath storePath = StorePath.parseFromUrl(createDocumentParam.getTemplatePath());
        byte[] sourceByte = fastFileStorageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
        if (null == sourceByte) {
            throw new IllegalArgumentException("模板路径错误，服务器读取不到该文件");
        }

        //封装数据到map
        HashMap<String, Object> data = getDocumentContext(createDocumentParam);

        //开启useSpringEL校验
        ConfigureBuilder builder = Configure.builder().useSpringEL(false);
        //绑定行循环的key
        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        data.keySet().forEach(key -> {
            if (key.endsWith("List")) {
                builder.bind(key, policy);
            }
            //软需列表
            if (key.contains("modularList")) {
                builder.bind("modulars", policy)
                        .bind("precondition", policy)
                        .bind("successScene", policy)
                        .bind("branchScene", policy)
                        .bind("constraint", policy)
                        .bind("dataInfo", policy)
                        .bind("dataDescriptionList", policy);
            }
            //数据库列表
            if (key.contains("docDBTableTemps")) {
                builder.bind("dBTableStructList", policy);
            }
            if (key.contains("functionUseCaseList")) {
                builder.bind("processStep", policy);
            }
        });

        //解析word模板并将结果输出到OutputStream
        InputStream template = new ByteArrayInputStream(sourceByte);
        ByteArrayOutputStream out = new ByteArrayOutputStream();
        WordUtil.parse(template, data, out, builder.build());

        //将生成的word文件上传到服务器
        byte[] bytes = out.toByteArray();
        InputStream inputStream = new ByteArrayInputStream(bytes);
        String upLoadPath = fastDFSUtil.uploadByIO(inputStream, createDocumentParam.getDocumentName() + ".doc").getFullPath();

        //保存文档信息
        DataSourceHolder.setDB(currentProject);
        document.setPath(upLoadPath);
        document.setUpdateTime(new Date());
        document.setCreateTime(new Date());
        documentMapper.insert(document);
    }

    @Override
    public HashMap<String, Object> getDocumentContext(CreateDocumentParam createDocumentParam) {
        String currentProject = DataSourceHolder.getDB().databaseName;
        Integer templateId = createDocumentParam.getTemplateId();

        //获取模板关联的修订记录，并将日期类型转换成字符串
        List<ReviseRecord> reviseRecordList = reviseRecordService.list(new QueryWrapper<ReviseRecord>().eq("template_id", templateId));
        List<DocReviseRecord> docReviseRecordList = new ArrayList<>();
        reviseRecordList.forEach(reviseRecord -> {
            docReviseRecordList.add(new DocReviseRecord(reviseRecord));
        });

        //获取模板关联的引用文档
        List<ReferenceDoc> referenceList = referenceDocService.list(new QueryWrapper<ReferenceDoc>().eq("template_id", templateId));

        //获取项目成员及角色
        DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
        List<ProjectMember> memberList = projectTeamService.memberRoleList(currentProject);

        //获取项目基本信息
        Project project = projectInfoService.getById(currentProject);
        if (project == null) {
            throw new IllegalArgumentException("该项目不存在");
        }

        //填充数据
        HashMap<String, Object> data = new HashMap<>();
        data.put("projectName", project.getName());
        data.put("projectId", project.getId());
        data.put("requirementProvider", project.getRequirementProvider());
        data.put("projectSecret", project.getSecretLevel().getValue());
        data.put("manufacture", project.getManufacture());
        data.put("projectProvider", project.getProjectProvider());
        data.put("projectDescription", project.getDescription());
        data.put("memberList", memberList);
        data.put("reviseRecordList", docReviseRecordList);
        data.put("referenceList", referenceList);
        data.put("currentDate", (new Date().getYear() + 1900) + "年" + (new Date().getMonth() + 1) + "月");
        data.put("version", createDocumentParam.getVersion());

        //获取模板类型
        TemplateTypeEnum templateType = Assert.notNull(docTemplateService.getById(templateId), "所选模板不存在").getType();

        //切换到项目库
        DataSourceHolder.setDB(currentProject);

        //根据模板类型填充数据:
        switch (templateType) {
            case SoftwareRequirementsSpecification: {
                softwareRequirementsSpecificationContext(data, currentProject);
                break;
            }
            case SoftwareDevelopment: {
                softwareDevelopmentContext(data);
                break;
            }
            case ConfigurationManagementPlan: {
                configurationManagementPlanContext(data);
            }
            case DatabaseDesignNotes: {
                databaseDesignNotesContext(createDocumentParam, data);
                break;
            }
            case SCTP: {
                SCTPContext(createDocumentParam.getTestPlanId(), data);
                break;
            }
            case SCTR: {
                SCTRContext(createDocumentParam.getTestPlanId(), data);
                break;
            }

            case SCTD: {
                SCTDContext(createDocumentParam.getTestPlanId(), data);
                break;
            }

            default:
                break;
        }
        return data;
    }

    private void SCTDContext(Integer testPlanId, HashMap<String, Object> data) {
        addHardwareList(data);
        addSoftwareList(data);
        addTestUseCase(data);
    }

    private void SCTPContext(Integer testPlanId, HashMap<String, Object> data) {
        addHardwareList(data);
        addSoftwareList(data);
        addRequirementList(data);

        //获取测试计划信息
        TestPlan plan = testPlanService.getById(2);
        if (plan == null)
            throw new IllegalArgumentException("选择的测试计划不存在!");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        data.put("testPlanStartTime", formatter.format(plan.getStartTime()));
        data.put("testPlanEndTime", formatter.format(plan.getEndTime()));

        //计算测试活动截止的时间点
        data.put("testAction1", formatter.format(plan.getStartTime()));
        data.put("testAction2", formatter.format(plan.getEndTime()));
        data.put("testAction3", formatter.format(plan.getEndTime()));
        data.put("testAction4", formatter.format(plan.getEndTime()));
        Integer days = DateUtil.daysBetween(plan.getStartTime(), plan.getEndTime());
        if (days > 2) {
            int round = (int) Math.round((days - 2) * 0.4);
            data.put("testAction2", formatter.format(DateUtil.addDays(plan.getStartTime(), round)));
            data.put("testAction3", formatter.format(DateUtil.addDays(plan.getEndTime(), -1)));
        }

    }

    private void SCTRContext(Integer testPlanId, HashMap<String, Object> data) {
        addHardwareList(data);
        //addRequirementList(data);

        //统计bug信息
        BugStatistics bugStatistics = testBugService.getBugStatistics(2);
        data.put("bugStatistics",BeanUtils.beanToMap(bugStatistics));

        //统计用例信息

        //获取测试计划信息
        TestPlan plan = testPlanService.getById(2);
        if (plan == null)
            throw new IllegalArgumentException("选择的测试计划不存在!");
        SimpleDateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
        data.put("testPlanStartTime", formatter.format(plan.getStartTime()));
        data.put("testPlanEndTime", formatter.format(plan.getEndTime()));

        //计算测试活动截止的时间点
        data.put("testAction1", formatter.format(plan.getStartTime()));
        data.put("testAction2", formatter.format(plan.getEndTime()));
        data.put("testAction3", formatter.format(plan.getEndTime()));
        data.put("testAction4", formatter.format(plan.getEndTime()));
        Integer days = DateUtil.daysBetween(plan.getStartTime(), plan.getEndTime());
        if (days > 2) {
            int round = (int) Math.round((days - 2) * 0.4);
            data.put("testAction2", formatter.format(DateUtil.addDays(plan.getStartTime(), round)));
            data.put("testAction3", formatter.format(DateUtil.addDays(plan.getEndTime(), -1)));
        }

    }

    private void configurationManagementPlanContext(HashMap<String, Object> map) {
        List<EnvSoftware> softwareList = softwareMapper.selectList(new QueryWrapper<>());
        map.put("softwareList", softwareList);
    }

    private void databaseDesignNotesContext(CreateDocumentParam createDocumentParam, HashMap<String, Object> map) {
        //测试数据库连接
        DBLinkParam dbLinkParam = new DBLinkParam()
                .setDbType(createDocumentParam.getDbType())
                .setDbName(createDocumentParam.getDbName())
                .setDbPath(createDocumentParam.getDbPath())
                .setPort(createDocumentParam.getPort())
                .setUserName(createDocumentParam.getUserName())
                .setPassword(createDocumentParam.getPassword());
        testDB(dbLinkParam);

        if (createDocumentParam.getDbType() == DataBaseTypeEnum.MYSQL) {
            List<DocDBTableTemp> docDBTableTempList = new ArrayList<>();

            //获取指定数据库中的所有表
            List<TableInfo> tableList = getAllTables(createDocumentParam.getDbName());

            List<TableFieldInfo> tableFieldInfoList = getTableFieldInfoList(createDocumentParam.getDbName());

            //表结构对应数据处理
            tableFieldInfoList.forEach(tableFieldInfo -> {
                //key=MUL则为外键
                //key=PRI 则为主键
                if (StringUtils.isNotBlank(tableFieldInfo.fieldKey)) {
                    if ("PRI".equals(tableFieldInfo.fieldKey)) {
                        tableFieldInfo.setFieldKey("是");
                    }
                    if ("MUL".equals(tableFieldInfo.fieldKey)) {
                        tableFieldInfo.setFieldKey("否");
                        tableFieldInfo.setFieldExtra("是");
                    } else {
                        tableFieldInfo.setFieldExtra("否");
                    }
                } else {
                    tableFieldInfo.setFieldKey("否");
                    tableFieldInfo.setFieldExtra("否");
                }
                tableFieldInfo.setFieldNull("YES".equals(tableFieldInfo.fieldNull) ? "是" : "否");
            });

            //将表结构按表名分组
            Map<String, List<TableFieldInfo>> allTableInfo = tableFieldInfoList.stream().collect(Collectors.groupingBy(TableFieldInfo::getTableName));

            tableList.forEach(table -> {
                //表结构数据处理
//                List<TableFieldInfo> tableFieldInfoList = getTableFieldInfoList(table.getName());
                DocDBTableTemp tableTemp = new DocDBTableTemp().setDBTableName(table.name).setDBTableComment(table.comment).setDBTableStructList(allTableInfo.get(table.getName()));
                docDBTableTempList.add(tableTemp);
            });

            map.put("docDBTableTemps", docDBTableTempList);
            map.put("DBName", createDocumentParam.getDbName());
        }
    }

    private void softwareRequirementsSpecificationContext(HashMap<String, Object> map, String currentProject) {
        //获取所有开发需求，并转换数据结构
        List<DevRequirement> devRequirementList = devRequirementService.list();
        List<DocRequirement> docRequirementList = new ArrayList<>();
        devRequirementList.forEach(devRequirement ->
                docRequirementList.add(new DocRequirement(devRequirement)
                        .setProjectId(currentProject)
                        .setPrototype(creatPictureRenderDataList(devRequirement.getPrototype(), devRequirement.getName())))
        );

        //性能需求列表
        List<DocRequirement> performanceReqList = docRequirementList.stream()
                .filter(requirement -> requirement.getRequirementType().equals(RequirementTypeEnum.PERFORMANCE))
                .collect(Collectors.toList());

        //功能需求按模块id分组
        Map<Integer, List<DocRequirement>> requirementMap = docRequirementList.stream()
                .filter(requirement -> requirement.getRequirementType().equals(RequirementTypeEnum.FUNCTION))
                .collect(Collectors.groupingBy(DocRequirement::getModularId));

        //获取所有模块列表,转换数据结构并填充需求列表信息
        List<DevModular> modularList = devModularService.list();
        List<DocModular> docModularList = new ArrayList<>();
        modularList.forEach(modular -> {
            docModularList.add(new DocModular(modular)
                    .setProjectId(currentProject)
                    .setDocRequirements(requirementMap.get(modular.getId())));
        });

        //模块列表转换成树形结构
        List<DocModular> modularTreeList = TreeUtil.treeOut(docModularList, DocModular::getId, DocModular::getParentId, DocModular::getModulars);

        //获取外部接口列表，并转换结构
        List<DevInterface> dev_externalInterfaces = devInterfaceService.devInterfaceListByType(InterfaceTypeEnum.EXTERNAL_INTERFACE.getCode());
        List<DocInterface> doc_externalInterfaceList = new ArrayList<>();
        dev_externalInterfaces.forEach(devInterface -> doc_externalInterfaceList.add(new DocInterface(devInterface, currentProject, map.get("projectName").toString())));

        //获取内部接口列表，并转换结构
        List<DevInterface> dev_internalInterfaces = devInterfaceService.devInterfaceListByType(InterfaceTypeEnum.INTERNAL_INTERFACE.getCode());
        List<DocInterface> doc_internalInterfaceList = new ArrayList<>();
        dev_internalInterfaces.forEach(devInterface -> doc_internalInterfaceList.add(new DocInterface(devInterface, currentProject, map.get("projectName").toString())));

        //获取数据需求列表，并转换结构
        List<DataRequirement> dataRequirements = dataRequirementService.list();
        List<DocDataRequirement> doc_dataRequirementList = new ArrayList<>();
        dataRequirements.forEach(requirement -> {
            doc_dataRequirementList.add(new DocDataRequirement(requirement));
        });

        //获取软硬件需求列表
        List<EnvHardware> hardwareList = hardwareMapper.selectList(new QueryWrapper<>());
        List<EnvSoftware> softwareList = softwareMapper.selectList(new QueryWrapper<>());

        map.put("modularList", modularTreeList);
        map.put("performanceReqList", performanceReqList);
        map.put("externalInterfaceList", doc_externalInterfaceList);
        map.put("internalInterfaceList", doc_internalInterfaceList);
        map.put("dataRequirementList", doc_dataRequirementList);
        map.put("hardwareList", hardwareList);
        map.put("softwareList", softwareList);
        map.put("docRequirementList", docRequirementList);
    }

    private void softwareDevelopmentContext(HashMap<String, Object> data) {
        //获取硬件需求列表，并转换结构
        addHardwareList(data);

        //获取软件需求列表，并转换结构
        addSoftwareList(data);

        //获取项目计划列表，并转换数据结构
        List<PlanTask> planTaskList = projectPlanService.getTaskListByWbs();
        List<DocPlanTask> docPlanTaskList = new ArrayList<>();
        planTaskList.forEach(planTask -> {
            docPlanTaskList.add(new DocPlanTask(planTask));
        });
        data.put("planTaskList", docPlanTaskList);
    }

    private void addTestUseCase(HashMap<String, Object> data) {
        List<TestUseCase> testUseCaseList = testUseCaseService.list();
        List<TestUseCase>  deploymentUseCaseList  = testUseCaseList.stream().filter(useCase -> useCase.getType().equals(UseCaseTypeEnum.DEPLOYMENT)).collect(Collectors.toList());
        List<TestUseCase>  functionUseCaseList    = testUseCaseList.stream().filter(useCase -> useCase.getType().equals(UseCaseTypeEnum.FUNCTION)).collect(Collectors.toList());
        List<TestUseCase>  performanceUseCaseList = testUseCaseList.stream().filter(useCase -> useCase.getType().equals(UseCaseTypeEnum.PERFORMANCE)).collect(Collectors.toList());
        List<TestUseCase>  interfaceUseCaseList = testUseCaseList.stream().filter(useCase -> useCase.getType().equals(UseCaseTypeEnum.INTERFACE)).collect(Collectors.toList());
        List<TestUseCase>  securityUseCaseList  = testUseCaseList.stream().filter(useCase -> useCase.getType().equals(UseCaseTypeEnum.SECURITY)).collect(Collectors.toList());

        data.put("deploymentUseCaseList", deploymentUseCaseList);
        data.put("functionUseCaseList", functionUseCaseList);
        data.put("performanceUseCaseList", performanceUseCaseList);
        data.put("interfaceUseCaseList", interfaceUseCaseList);
        data.put("securityUseCaseList", securityUseCaseList);



    }

    private void addHardwareList(HashMap<String, Object> data) {
        //获取硬件需求列表，并转换结构
        List<EnvHardware> hardwareList = hardwareMapper.selectList(new QueryWrapper<>());
        List<DocEnvHardware> doc_hardwareList = new ArrayList<>();
        hardwareList.forEach(hardware -> {
            doc_hardwareList.add(new DocEnvHardware(hardware));
        });
        data.put("hardwareList", doc_hardwareList);
    }

    private void addRequirementList(HashMap<String, Object> data) {
        //获取所有开发需求，并转换数据结构
        List<DevRequirement> devRequirementList = devRequirementService.list();

        //功能需求列表
        List<DevRequirement> functionReqList = devRequirementList.stream()
                .filter(requirement -> requirement.getRequirementType().equals(RequirementTypeEnum.FUNCTION))
                .collect(Collectors.toList());
        data.put("functionReqList", functionReqList);

        //性能需求列表
        List<DevRequirement> performanceReqList = devRequirementList.stream()
                .filter(requirement -> requirement.getRequirementType().equals(RequirementTypeEnum.PERFORMANCE))
                .collect(Collectors.toList());
        data.put("performanceReqList", performanceReqList);

        //安装部署需求列表
        List<DevRequirement> deployReqList = devRequirementList.stream()
                .filter(requirement -> requirement.getRequirementType().equals(RequirementTypeEnum.DEPLOY))
                .collect(Collectors.toList());
        data.put("deployReqList", deployReqList);

        //安全需求列表
        List<DevRequirement> securityReqList = devRequirementList.stream()
                .filter(requirement -> requirement.getRequirementType().equals(RequirementTypeEnum.SECURITY))
                .collect(Collectors.toList());
        data.put("securityReqList", securityReqList);

        //获取接口列表
        List<DevInterface> interfaceReqList = devInterfaceService.devInterfaceListByType(null);
        data.put("interfaceReqList", interfaceReqList);
    }

    private void addSoftwareList(HashMap<String, Object> data) {
        //获取软件需求列表，并转换结构
        List<EnvSoftware> softwareList = softwareMapper.selectList(new QueryWrapper<>());
        List<DocEnvSoftware> doc_softwareList = new ArrayList<>();
        softwareList.forEach(software -> {
            doc_softwareList.add(new DocEnvSoftware(software));
        });
        data.put("softwareList", doc_softwareList);
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
    public List<TableInfo> getAllTables(String dbName) {
        return documentMapper.getAllTables(dbName);
    }

    @Override
    public List<TableFieldInfo> getTableFieldInfoList(String DBName) {
        return documentMapper.getTableStructureList(DBName);
    }

    @Override
    public Boolean testDB(DBLinkParam dbLinkParam) {
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
            getAllTables(dbLinkParam.getDbName());
        } catch (Exception e) {
            throw new IllegalArgumentException("数据库连接失败，请重新确认参数是否正确");
        } finally {
            dynamicDataSource.deleteDataSourceByName(dataSourceName);
        }
        return true;
    }

    /**
     * 文档校验
     */
    private void documentChecking(Document document) {
        if (getDocumentByDirectoryId(document.getDirectoryId()) == null)
            throw new IllegalArgumentException("生成文档所属目录id不存在");

        if (existSameNameDoc(document.getDirectoryId(), document.getName(), "add"))
            throw new IllegalArgumentException("该目录下已存在同名文档");
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

    private List<PoitlPicture> creatPictureRenderDataList(List<String> prototype, String requireName) {
        if (prototype == null || prototype.size() < 1) {
            return null;
        }
        //用数据库存储的路径获取对应的图像输入流
        List<PoitlPicture> pictureList = new ArrayList<>();
        try {
            for (int i = 0; i < prototype.size(); i++) {
                String eachPrototype = prototype.get(i);
                if (StringUtils.isBlank(eachPrototype)) {
                    continue;
                }
                StorePath storePath = StorePath.parseFromUrl(eachPrototype);
                byte[] sourceByte = fastFileStorageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
                InputStream streamImg = new ByteArrayInputStream(sourceByte);
                //图片尺寸设置
                String pictureName = prototype.size() > 1 ? requireName + "原型设计图" + (i + 1) : requireName + "原型设计图";
                pictureList.add(new PoitlPicture().setStreamImg(Pictures.ofStream(streamImg, PictureType.JPEG)
                        .size(585, 305).create()).setPictureName(pictureName));
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return pictureList;
    }

}