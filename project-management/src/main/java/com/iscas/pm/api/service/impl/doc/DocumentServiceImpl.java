package com.iscas.pm.api.service.impl.doc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.domain.proto.storage.DownloadByteArray;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import com.iscas.pm.api.mapper.doc.DocumentMapper;
import com.iscas.pm.api.mapper.env.EnvHardwareMapper;
import com.iscas.pm.api.mapper.env.EnvSoftwareMapper;
import com.iscas.pm.api.mapper.projectPlan.ProjectPlanMapper;
import com.iscas.pm.api.model.doc.*;
import com.iscas.pm.api.model.doc.param.CreateDocumentParam;
import com.iscas.pm.api.model.env.EnvHardware;
import com.iscas.pm.api.model.env.EnvSoftware;
import com.iscas.pm.api.model.projectPlan.PlanTask;
import com.iscas.pm.api.service.DocumentService;
import com.iscas.pm.api.service.ProjectInfoService;
import com.iscas.pm.api.service.ProjectPlanService;
import com.iscas.pm.api.util.DocumentHandler;
import com.iscas.pm.api.util.FastDFSUtil;
import com.iscas.pm.api.util.FileCovertUtils;
import com.iscas.pm.common.core.util.RedisUtil;
import com.iscas.pm.common.core.web.filter.RequestHolder;
import com.iscas.pm.common.db.separate.datasource.DefaultDataSource;
import com.iscas.pm.common.db.separate.holder.DataSourceHolder;
import org.apache.commons.io.FilenameUtils;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.File;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
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


    @Override
    public Document addLocalDocument(Document document) {
        if (getDocumentByDirectoryId(document.getDirectoryId()) == null)
            throw new IllegalArgumentException("所属目录不存在");
        if (existSameNameDoc(document.getDirectoryId(), document.getName(), "add"))
            throw new IllegalArgumentException("该目录下已存在同名文档");
        document.setPath(document.getPath());
        documentMapper.insert(document);
        return document;
    }

//    @Override
//    public Document addLinkDocument(Document document) {
//        if (getDocumentByDirectoryId(document.getDirectoryId()) == null)
//            throw new IllegalArgumentException("所属目录不存在");
//
//        if (existSameNameDoc(document.getDirectoryId(), document.getName(), "add"))
//            throw new IllegalArgumentException("该目录下已存在同名文档");
//
//        documentMapper.insert(document);
//        return document;
//    }

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

//
//    @Override
//    public String uploadDocument(MultipartFile file) throws IOException {
//        //文件存入FastDFs
//        StorePath path = fastDFSUtil.upload(file);
//        //路径存储到redis中
//        redisUtil.set(path.getFullPath(), null);
//        //设置失效时间  (数值待定)
//        redisUtil.expire(path.getFullPath(), 1000);
//        return path.getFullPath();
//    }


    @Override
    public void createDocument(CreateDocumentParam createDocumentParam) throws IOException {
        //前端传入参数只含共用的信息，数据库查找的信息根据模板id选择性填充

        //拿到服务器中模板的存储路径
        StorePath storePath = StorePath.parseFromUrl(createDocumentParam.getTemplatePath());
        byte[] sourceByte = fastFileStorageClient.downloadFile(storePath.getGroup(), storePath.getPath(), new DownloadByteArray());
        if (null == sourceByte) {
            throw new IllegalArgumentException("模板路径错误，服务器读取不到该文件");
        }
        //从服务器上把模板下载到本地 ：
        String templatePath = "D:/file/";   //暂时改为F盘
        String tempName = createDocumentParam.getDocumentName() + "Template.ftl";
        try {
            File file = new File(templatePath + tempName);//文件路径（路径+文件名）
            if (!file.exists()) {   //文件不存在则创建文件，先创建目录
                File dir = new File(file.getParent());
                dir.mkdirs();
                file.createNewFile();
            }
            FileOutputStream outStream = new FileOutputStream(file); //文件输出流将数据写入文件
            outStream.write(sourceByte);
            outStream.close();
        } catch (Exception e) {
            e.printStackTrace();
        }

        //读取当前项目相关信息：
        //获取当前项目id：
        DataSourceHolder.setDB("default");
        ProjectDetailInfo projectDetailInfo = projectInfoService.getProjectDetailInfo(createDocumentParam.getCurrentProjectId());
        HashMap<String, Object> map = new HashMap<>();

        //填充通用内容：


        //根据模板id填充特定内容:
        if (createDocumentParam.getTemplateId() == 1) {
            List<ReviseRecord> reviseRecordListList = createDocumentParam.getReviseRecordList();
            List<ReferenceDoc> referenceList = createDocumentParam.getReferenceDocList();
            //project获取：
            map.put("项目名称", projectDetailInfo.getBasicInfo().getName());
            map.put("项目编号", projectDetailInfo.getBasicInfo().getId());
//            map.put("合同编号", projectDetailInfo.getBasicInfo().getContractId());
            map.put("项目阶段", projectDetailInfo.getBasicInfo().getStatus());
            map.put("需求提出方", projectDetailInfo.getBasicInfo().getRequirementProvider());
            map.put("项目密级", projectDetailInfo.getBasicInfo().getSecretLevel());
            map.put("研制单位", projectDetailInfo.getBasicInfo().getManufacture());
            map.put("项目提出方", projectDetailInfo.getBasicInfo().getProjectProvider());


            DataSourceHolder.setDB(createDocumentParam.getCurrentProjectId());
            //Hardware项目硬件环境信息获取
            List<EnvHardware> hardwareList = hardwareMapper.selectList(new QueryWrapper<>());
            //Software项目软件环境信息获取
            List<EnvSoftware> softwareList = softwareMapper.selectList(new QueryWrapper<>());
            //项目计划信息获取
            List<PlanTask> planTaskList = projectPlanService.getTaskListByWps();

            // 总体进度计划   planTask  wbs编号为一级的： <#list planTaskList as planTask>   结束用  </#list>
            //List<PlanTask> planTaskList = allPlanList.stream().filter(plan -> plan.getWbs().split("\\.").length < 2).collect(Collectors.toList());


//            // planTask  wbs编号为二级的：
//                //系统分析与设计阶段任务计划列表：  wbs 1.x  <#list planTaskList1 as planTask1>
//            //需要添加非空校验(目前  如果没有二级编号就会报错)
//            List<PlanTask> planTaskList1 = allPlanList.stream().filter(plan -> {
//                    String[] split = plan.getWbs().split("\\.");
//                    return  (split.length>1&&split[0].equals("1"));}).collect(Collectors.toList());
//            //需求分析阶段任务计划（WBS：2）：  wbs 2.x  <#list planTaskList2 as planTask2>
//            List<PlanTask> planTaskList2 = allPlanList.stream().filter(plan2 -> {
//                String[] split = plan2.getWbs().split("\\.");
//                return  (split.length>1&&split[0].equals("2"));}).collect(Collectors.toList());
//            map.put("planTaskList1",planTaskList1);
//            map.put("planTaskList2",planTaskList2);
            map.put("hardwareList", hardwareList);
            map.put("softwareList", softwareList);
            map.put("planTaskList", planTaskList);

            //用户输入：
            map.put("项目标识", createDocumentParam.getProjectMark());
            map.put("软件负责人", createDocumentParam.getSoftwareManager());
            map.put("本文档版本号", createDocumentParam.getVersion());
            map.put("单位名称", createDocumentParam.getUnit());
            map.put("软件开发组", createDocumentParam.getSoftwareDevTeam());

            //前端传入：//模板需要加 #list
            map.put("reviseRecordListList", reviseRecordListList);
            map.put("referenceList", referenceList);
        }

//      map.put("引用文档",referenceList);
        //输出到服务器和D:/outPutDoc.doc
        String docName = createDocumentParam.getDocumentName() + ".doc";
        String upLoadPath = documentHandler.createDoc(map, docName, templatePath, tempName);
        //生成的文档上传到fastDfs  返回存储路径存储到mysql里（存储到document表里）
        Document autoDoc = new Document();
        autoDoc.setDirectoryId(createDocumentParam.getDirectoryId());
        autoDoc.setCreateTime(new Date());
        autoDoc.setPath(upLoadPath);
        autoDoc.setUpdateTime(new Date());
        autoDoc.setUploader(RequestHolder.getUserInfo().getEmployeeName());
        autoDoc.setName(createDocumentParam.getDocumentName());
        autoDoc.setType(DocumentTypeEnum.GENERATE);
        autoDoc.setVersion(createDocumentParam.getVersion());
        //如果不设置目录id会报：不符合数据库约束(外键)
        if (addLocalDocument(autoDoc) == null) {
            throw new IOException("自动上传文档失败");
        }
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




