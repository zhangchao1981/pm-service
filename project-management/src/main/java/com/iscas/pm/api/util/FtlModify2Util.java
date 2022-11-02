package com.iscas.pm.api.util;

import cn.hutool.core.io.FileUtil;
import cn.hutool.core.io.file.FileAppender;
import cn.hutool.core.io.file.FileReader;

import java.util.List;

/**
 * 将分离的占位符去除、变量值重新拼装
 * 待补充：表格填充需手动在 表开始的</w:tr>后面加上    <#list recordList as reviseRecord>
 * 表结束的</w:tr>后面加上  </#list>
 * data类型需要在模板上加 ：?string('dd.MM.yyyy HH:mm:ss')
 * 例如：<w:t>${reviseRecord.date?string('dd.MM.yyyy HH:mm:ss')}</w:t>
 *
 * @author luvJie-7c
 * @date 2022-7-26
 */

//待完善：  date类型的自动处理 ：识别date关键字，后面添加?string('dd.MM.yyyy HH:mm:ss')    注意排除： <w:validateAgainstSchema/>
//待完善：  list集合填充
//特殊情况：如果表格在 ${}  后面还有非填充内容，表的结尾就会变成       </w:tr>
public class FtlModify2Util {
    //表头是 </w:tr>且下个非空字符串是<w:tr  表尾是  </w:tr>且下个字符串是  </w:tbl>
    public static void main(String[] args) {
        String tempPath = "D:\\模板处理测试\\";
        String tempName = "02软件开发计划FTL.ftl";
        //文件读取-FileReader
        //默认UTF-8编码，可以在构造中传入第二个参数作为编码
        FileReader fileReader = new FileReader(tempPath + "first" + tempName);
        //从文件中读取每一行数据
        List<String> strings = fileReader.readLines();
        //文件追加-FileAppender
        //destFile – 目标文件
        //capacity – 当行数积累多少条时刷入到文件
        //isNewLineMode – 追加内容是否为新行
        FileAppender appender = new FileAppender(FileUtil.newFile(tempPath + "sec" + tempName), 16, true);
        //遍历得到每一行数据

        //判断逻辑   遇到</w:tr>就开始找，找到${}且里面包含.的  就在<w:tr wsp前面加 	<#list planTaskList1 as planTask1>,在后面  </w:tr>    </w:tbl>中间加 </#list>
        for (int i = 0; i < strings.size(); i++) {
            String string = strings.get(i);

            //判断表头，不是表头的先加进新文件
            int tableStart = string.indexOf("</w:tr>");
            if (tableStart < 0) {
                appender.append(string);
                continue;
            } else {
                //判断下一个不为空的字符串是不是<w:tr
                // while tablestart==null ++ 超出sting i++   tablestart=0  直到找到下一个不为空的字符
                int tableHead = tableStart + 7;
                int j = i;
                String findString = string;
                if (tableHead >= string.length()) {
                    tableHead = 0;
                    findString = strings.get(++j);
                }
                while (findString.charAt(tableHead) == ' ') {
                    tableHead++;
                    if (tableHead >= findString.length()) {
                        tableHead = 0;
                        findString = strings.get(++j);
                    }
                }
                //判断不是表头
                if (!findString.substring(tableHead, findString.length()).startsWith("<w:tr")) {
                    appender.append(string);
                    continue;
                } else {
                    //是表头   i对应</w:tr>所在字符串   j对应<w:tr所在字符串   tableHead对应<w:tr首字母索引
                    StringBuilder sb = new StringBuilder();             //</w:tr>  <w:tr>  是表头  </w:tr> </w:tbl>是表尾
                    int start = string.indexOf("</w:tr>") + 7;    //abc<>def     abc<>   加到append里   def加到sb里
                    //找到要判断插入列表头的位置
                    if (start == string.length()) {
                        appender.append(string);
                        i++;
                        string = strings.get(i);
                    } else {
                        appender.append(string.substring(0, start));
                        string = string.substring(start, string.length());
                    }

                    //列表中是否有 自定义变量
                    Boolean flag = new Boolean(false);
                    while (!string.contains("</w:tbl>")) {  //只要不到  表尾，就继续找
                        sb.append(string);
                        if (string.indexOf(".") > string.indexOf("$") && string.indexOf("$") > -1) {
                            //有自定义填充属性的列表：
                            //拼接列表头  <#list planTaskList1 as planTask1>
                            String name = string.substring(string.indexOf("{") + 1, string.indexOf("."));
                            String head = "<#list " + name + "List" + " as " + name + ">";
                            appender.append(head);
                            flag = true;
                            i++;
                            string = strings.get(i);
                            break;
                            //只找第一个，找到就出去
                        }
                        i++;
                        string = strings.get(i);
                    }
                    //出来的肯定有列表尾   看是否含自定义变量?    不包含的   列表尾的判断依据： </w:tbl>
                    // 是就加上</#list>
                    if (flag) {  //sb 加到</w:tr>前面一行， string指向后面一行                  I=2355 和string一同指向  name的下一行
                        //当前sb添加到了第一个属性这里，继续往下找
                        while (!string.contains("</w:tbl>")) {
                            sb.append(string);
                            i++;
                            string = strings.get(i);
                        }
                        //对尾部处理一下
                        string = string.replaceFirst("</w:tbl>", "</#list></w:tbl>");
                    }
                    appender.append(sb.toString());
                    appender.append(string);
                }
            }
        }
        appender.flush();
        appender.toString();
    }
}
