package com.iscas.pm.api.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileAppender;
import cn.hutool.core.io.file.FileReader;

import java.util.HashMap;
import java.util.HashSet;
import java.util.List;

/**
 * 将分离的占位符去除、变量值重新拼装
 *  //待补充：表格填充需手动在 表开始的</w:tr>前面加上    <#list recordList as reviseRecord>
 *                                      表结束的</w:tr>前面加上  </#list>
 *  data类型需要在模板上加 ：?string('dd.MM.yyyy HH:mm:ss')
 * 例如：<w:t>${reviseRecord.date?string('dd.MM.yyyy HH:mm:ss')}</w:t>
 * @author luvJie-7c
 * @date 2022-7-26
 *
 */
public class FtlModifyUtil {
    public static void main(String[] args) {
            //文件读取-FileReader
            //默认UTF-8编码，可以在构造中传入第二个参数作为编码
        FileReader fileReader = new FileReader("F:\\模板处理测试\\8月31日文档生成测试05.ftl");
            //从文件中读取每一行数据
            List<String> strings = fileReader.readLines();
            //文件追加-FileAppender
            //destFile – 目标文件
            //capacity – 当行数积累多少条时刷入到文件
            //isNewLineMode – 追加内容是否为新行
        FileAppender appender = new FileAppender(FileUtil.newFile("F:\\模板处理测试\\firstNEW8月31日文档生成测试05.ftl"), 16, true);
        HashSet<String> dataFillName = new HashSet<>();
        //遍历得到每一行数据
            for (String string : strings) {
                //判断每一行数据中不包含'$'的数据先添加进新文件
                if (!string.contains("$")) {
                    appender.append(string);
                    continue;
                }
                //如果一行数据中包含'$'变量符将替换为'#$'
                string = string.replaceAll("\\$", "#\\$");
                //然后以'#'切割成每一行（数组）,这样一来'$'都将在每一行的开头
                String[] ss = string.split("#");
                // 同一行的数据写到同一行,文件追加自动换行了(最后的完整数据)
                StringBuilder sb = new StringBuilder();
                //遍历每一行（数组ss）
                for (int i = 0; i < ss.length; i++) {
                    //暂存数据
                    String s1 = ss[i];
                    //将不是以'$'开头的行数据放进StringBuilder
                    if (!s1.startsWith("$")) {
                        sb.append(s1);
                        continue;
                    }
                    //date类型转换
                    s1=s1.replaceAll("date","date?string('dd.MM.yyyy HH:mm:ss')");


                    //被分离的数据一般都是'${'这样被分开
                    //匹配以'$'开头的变量找到'}' 得到索引位置
                    int i1 = s1.lastIndexOf("}");
                    //先切割得到这个完整体
                    String substr = s1.substring(0, i1 + 1);
                    //把变量追加到StringBuilder
                    sb.append(substr.replaceAll("<[^>]+>", ""));
                    //再将标签数据追加到StringBuilder包裹变量
                    dataFillName.add(substr.replaceAll("<[^>]+>", ""));
                    sb.append(s1.substring(i1 + 1));
                }
                appender.append(sb.toString());
            }
            appender.flush();
            appender.toString();
        System.out.println(dataFillName);
    }
}
