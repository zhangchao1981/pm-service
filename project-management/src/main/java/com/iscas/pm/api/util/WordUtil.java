package com.iscas.pm.api.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.IORuntimeException;
import cn.hutool.core.lang.Assert;
import cn.hutool.core.map.MapUtil;
import com.deepoove.poi.XWPFTemplate;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.Map;

public class WordUtil {
    public static void main(String[] args) throws IOException {
        XWPFTemplate compile = XWPFTemplate.compile("D:\\文档\\TestPoi-Tl.doc");

        parse("D:\\文档\\TestPoi-Tl.doc",
                MapUtil.of("title", "Hello poi-tl模版引擎"),
                "D:\\TestPoi-Tl.doc");
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
}