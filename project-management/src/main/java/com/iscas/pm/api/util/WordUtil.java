package com.iscas.pm.api.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Assert;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.data.PictureRenderData;
import com.deepoove.poi.data.PictureType;
import com.deepoove.poi.data.Pictures;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.iscas.pm.api.model.Family;
import com.iscas.pm.api.model.TestChild;
import com.iscas.pm.api.model.TestPerson;
import com.iscas.pm.api.model.dev.DataRequirement;
import com.iscas.pm.api.model.dev.DataRequirementInfo;
import com.iscas.pm.api.model.dev.DataTypeEnum;

import java.io.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class WordUtil {
//@Autowired
//DevRequirementService devRequirementService;
//
    public static void main(String[] args) throws IOException, IllegalAccessException {
//      MapUtil.of("title", "Hello poi-tl模版引擎")
        HashMap<String, Object> data = new HashMap<>();
        PictureRenderData renderData = Pictures.ofStream(new FileInputStream(new File("D:\\中科院资料\\图片测试.jpg")), PictureType.JPEG)
                .size(600, 720).create();
        data.put("streamImg",renderData);
        List<DataRequirementInfo> dataRequirementInfoList = new ArrayList<>();
        DataRequirementInfo dataRequirementInfo = new DataRequirementInfo();
        dataRequirementInfo.setDataType(DataTypeEnum.ARRAYLIST);
        dataRequirementInfoList.add(dataRequirementInfo);
        data.put("dataRequirementInfoList",dataRequirementInfoList);

//        List<ReviseRecord> reviseRecordList =new ArrayList<>();
//        //新建一个类  把日期属性改成String类型，并用  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");转换后存储过来
//
//        ReviseRecord reviseRecord = new ReviseRecord();
//        Date date = new Date();
//        //日期类型对象  在不改变实体类和模板的情况下   输出正确格式
//
//        reviseRecord.setId(5).setMender("lichang").setTemplateId(1).setVersion("1.0").setDate(date).setApprover("lichang").setNotes("test");
//        reviseRecordList.add(reviseRecord);
//        List<DocReviseRecord> reviseRecordListNew =new ArrayList<>();
//        reviseRecordList.forEach(revise->{
//            reviseRecordListNew.add(new DocReviseRecord(revise));
//        });


//        String currentProject = DataSourceHolder.getDB().databaseName;
//        DataSourceHolder.setDB(currentProject);
//        List<DevModular> modularList = devRequirementService.devRequirementListOrTaskList(new DevRequirementQueryParam().setQueryType("DevRequirement"));
//        map.put("modularList",modularList);//一级模块


//        data.put("项目名称","poil测试");
//        data.put("reviseRecordList",reviseRecordListNew);
        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();

        Configure config = Configure.builder().useSpringEL(false)
                .bind("dataRequirementInfoList", policy).build();
        parse("D:\\中科院资料\\10月18日目标文档\\图片插入测试.docx",config,data,"D:\\中科院资料\\10月18日目标文档\\图片插入测试输出.docx");
    }


    /**
     * 解析word文件并将结果输出到OutputStream(含表格)
     *
     * @param in      输入流
     * @param dataMap 数据Map
     * @param out     输出流
     */
    public static void parse(InputStream in,
                             Map<String, Object> dataMap,
                             OutputStream out,Configure config) {
        Assert.notNull(in, "in can not be null");
        Assert.notNull(out, "out can not be null");
        Assert.notNull(dataMap, "dataMap can not be null");
        try {
            XWPFTemplate temp = XWPFTemplate.compile(in,config).render(dataMap);
            temp.writeAndClose(out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new IORuntimeException(e);
                }
            }
        }
    }

    /**
     * 解析word文件并将结果输出到OutputStream
     *
     * @param in      输入流
     * @param dataMap 数据Map
     * @param out     输出流
     */
    public static void parse(InputStream in,
                             Map<String, Object> dataMap,
                             OutputStream out) {
        Assert.notNull(in, "in can not be null");
        Assert.notNull(out, "out can not be null");
        Assert.notNull(dataMap, "dataMap can not be null");
        try {
            XWPFTemplate.compile(in).render(dataMap).writeAndClose(out);
        } catch (IOException e) {
            throw new IORuntimeException(e);
        } finally {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e) {
                    throw new IORuntimeException(e);
                }
            }
        }
    }






    /**
     * 解析word文件并将结果输出到OutputStream
     *
     * @param templateFilePath word文件路径，不存在该文件会报错
     * @param dataMap          数据Map
     * @param outFilePath      输出文件路径。不存在该文件会自动创建
     */
    public static void parse(String templateFilePath,
                             Map<String, Object> dataMap,
                             String outFilePath) {
        InputStream inputStream = FileUtil.getInputStream(templateFilePath);
        OutputStream out = FileUtil.getOutputStream(FileUtil.touch(outFilePath));
        parse(inputStream, dataMap, out);
    }



    /**
     * 解析word文件(含表格)并将结果输出到OutputStream
     *
     * @param templateFilePath word文件路径，不存在该文件会报错
     * @param dataMap          数据Map
     * @param outFilePath      输出文件路径。不存在该文件会自动创建
     */
    public static void parse(String templateFilePath,
                             Configure config ,
                             Map<String, Object> dataMap,
                             String outFilePath) {
        InputStream inputStream = FileUtil.getInputStream(templateFilePath);
        OutputStream out = FileUtil.getOutputStream(FileUtil.touch(outFilePath));
        parse(inputStream, dataMap, out,config);
    }

}