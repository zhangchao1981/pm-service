package com.doc.util;

/**
 * @author by zhangchao
 * @date 2022/6/16.
 */

import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;

import java.io.*;
import java.util.Map;

public class DocumentHandler {
//
//    private Configuration configuration = null;
//
//    public DocumentHandler() {
//        configuration = new Configuration();
//        configuration.setDefaultEncoding("utf-8");
//    }

    /**
     * 根据数据和模板生成word文档
     *
     * @param dataMap  要填入模板的数据
     * @param fileName 生成文件的路径
     * @throws UnsupportedEncodingException
     */
    public static void createDoc(Map<String, Object> dataMap, String fileName) throws UnsupportedEncodingException {
        Configuration configuration = new Configuration();
        configuration.setDefaultEncoding("utf-8");
        //装载模板

        configuration.setClassForTemplateLoading(DocumentHandler.class, "/template");
        Template template = null;
        try {
            //test.ftl为要装载的模板   这里模板写死了，后面要写活
            template = configuration.getTemplate("test.ftl");
        } catch (IOException e) {
            e.printStackTrace();
        }

        //输出文档路径及名称
        File outFile = new File(fileName);
        Writer out = null;
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(outFile);
            OutputStreamWriter oWriter = new OutputStreamWriter(fos, "UTF-8");
            //这个地方对流的编码不可或缺，使用main（）单独调用时，应该可以，但是如果是web请求导出时导出后word文档就会打不开，并且包XML文件错误。主要是编码格式不正确，无法解析。
            //out = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(outFile)));
            out = new BufferedWriter(oWriter);
        } catch (FileNotFoundException e1) {
            e1.printStackTrace();
        }

        try {
            template.process(dataMap, out);
            out.close();
            fos.close();
        } catch (TemplateException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {

        }

        //System.out.println("---------------------------");
    }
}

