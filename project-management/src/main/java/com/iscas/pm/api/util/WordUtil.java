package com.iscas.pm.api.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.deepoove.poi.XWPFTemplate;
import com.deepoove.poi.config.Configure;
import com.deepoove.poi.plugin.table.LoopRowTableRenderPolicy;
import com.iscas.pm.api.mapper.doc.ReviseRecordMapper;
import com.iscas.pm.api.model.doc.ReviseRecord;
import com.iscas.pm.api.model.doc.data.DocReviseRecord;
import com.iscas.pm.api.service.ReviseRecordService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import javax.print.Doc;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.lang.reflect.Field;
import java.text.SimpleDateFormat;
import java.util.*;

public class WordUtil {

//



    public static void main(String[] args) throws IOException, IllegalAccessException {
//        MapUtil.of("title", "Hello poi-tl模版引擎")
        HashMap<String, Object> data = new HashMap<>();
        List<ReviseRecord> reviseRecordList =new ArrayList<>();
        //新建一个类  把日期属性改成String类型，并用  SimpleDateFormat("yyyy-MM-dd HH:mm:ss");转换后存储过来

        ReviseRecord reviseRecord = new ReviseRecord();
        Date date = new Date();
        //日期类型对象  在不改变实体类和模板的情况下   输出正确格式

        reviseRecord.setId(5).setMender("lichang").setTemplateId(1).setVersion("1.0").setDate(date).setApprover("lichang").setNotes("test");
        reviseRecordList.add(reviseRecord);

        List<DocReviseRecord> reviseRecordListNew =new ArrayList<>();
        reviseRecordList.forEach(revise->{
            reviseRecordListNew.add(new DocReviseRecord(revise));
        });


//
//
//        List< Map<String, String>> newReviseRecordList =new ArrayList<>();
//        reviseRecordList.forEach(revise->{
//            Map<String, String> newReviseRecord = new HashMap<String, String>();
//            Field[] declaredFields = revise.getClass().getDeclaredFields();
//            for (Field field : declaredFields) {
//                field.setAccessible(true);
//                try {
//                    newReviseRecord.put(field.getName(), (String) field.get(revise));
//                } catch (IllegalAccessException e) {
//                    throw new RuntimeException(e);
//                }
//            }
//            newReviseRecordList.add(newReviseRecord);
//        });
//



        data.put("项目名称","poil测试");
        data.put("reviseRecordList",reviseRecordListNew);
        LoopRowTableRenderPolicy policy = new LoopRowTableRenderPolicy();
        Configure config = Configure.builder()
                .bind("reviseRecordList", policy).build();

        parse("F:\\POI测试\\表格文本替换测试文件.docx",config,data,
                "F:\\POI测试\\OutPutPoi-Tl.doc");
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
            XWPFTemplate.compile(in,config).render(dataMap).writeAndClose(out);
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