package com.iscas.pm.api.util;

/**
 * @author by zhangchao
 * @date 2022/6/16.
 */

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.github.tobato.fastdfs.service.FastFileStorageClient;
import freemarker.template.Configuration;
import freemarker.template.Template;
import freemarker.template.TemplateException;
import freemarker.template.Version;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.*;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;
import java.util.Map;
@Component
public class DocumentHandler {
    @Autowired
    private FastDFSUtil fastDFSUtil;
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
    public String createDoc(Map<String, Object> dataMap, String fileName, String tempName) throws IOException {
        Configuration configuration = new Configuration(new Version("2.3.28"));
        configuration.setDefaultEncoding("utf-8");
        //装载模板    设置的服务器模板存储位置为 D:/file
        configuration.setDirectoryForTemplateLoading(new File("F:/file"));
        Template template = null;

        //测试生成本地文件：输出路径为D:/file/
        File outFile = new File("F:/file/"+fileName);   //测试用
        FileOutputStream fos = null;                               //测试用
        Writer out = null;                                         //测试用

        try {
            //test.ftl为要装载的模板
            template = configuration.getTemplate(tempName, "UTF-8");
            //输出到服务器
            fos = new FileOutputStream(outFile);                    //测试用
            OutputStreamWriter oWriter = new OutputStreamWriter(fos, StandardCharsets.UTF_8);//测试用
            //这个地方对流的编码不可或缺，使用main（）单独调用时，应该可以，但是如果是web请求导出时导出后word文档就会打不开，并且包XML文件错误。主要是编码格式不正确，无法解析。
            out = new BufferedWriter(oWriter);                        //测试用

            //将处理的结果转为byte[]，在内存中存储文件内容
            ByteArrayOutputStream os = new ByteArrayOutputStream();
            Writer writer = new OutputStreamWriter(os, StandardCharsets.UTF_8);
            template.process(dataMap, writer);
            byte[] bytes = os.toByteArray();
            //拿到文件内容，输出到fastDFS服务器上
            InputStream inputStream = new ByteArrayInputStream(bytes);
            StorePath storePath = fastDFSUtil.uploadByIO(inputStream, fileName );
            //再将结果也存到本地磁盘上(待删，测试用)
            template.process(dataMap, out);
            return storePath.getFullPath();
        } catch (IOException | TemplateException e1) {
            e1.printStackTrace();
        } finally {
            out.close();
            fos.close();
        }
        return null;
    }
}

