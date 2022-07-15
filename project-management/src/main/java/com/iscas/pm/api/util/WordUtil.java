package com.iscas.pm.api.util;

import java.io.UnsupportedEncodingException;
import java.util.HashMap;
import java.util.Map;

/**
 * @author by zhangchao
 * @date 2022/6/16.
 * word文档工具类
 */
public class WordUtil {
    public static void main(String[] args) throws UnsupportedEncodingException {
        DocumentHandler documentHandler = new DocumentHandler();
        DocumentHandler.createDoc(getData(),"D:/test1.doc");//输出到F:/test.doc
    }

    private static Map<String,Object> getData(){//要输入到模板上的数据

        Map<String,Object> dataMap = new HashMap<>();
        //等会再从mysql里拿数据填进去
        //这里是普通息⽤于普通⽂本处理
        dataMap.put("id","id假数据");
        dataMap.put("pjname","pjname假数据");
        dataMap.put("pjnumber","pjnumber假数据");
        dataMap.put("pjstage","pjstage假数据");

        //输入 数据封装对象   这里是用于表格和图片处理
//        Developmentplan case1 = new Developmentplan("0001","用例1");
//        Developmentplan case2 = new Developmentplan("0002","用例2");
//        List<Developmentplan> caseList = new ArrayList<>();
//        caseList.add(case1);
//        caseList.add(case2);
//        Model subModel = new Model("子模块1",caseList,new ArrayList<>());
//        List<Model> subModels = new ArrayList<>();
//        subModels.add(subModel);
//        Model model1 = new Model("模块1",new ArrayList<>(),subModels);
//        Model model2 = new Model("模块2",caseList,new ArrayList<>());
//
//        List<Model> models = new ArrayList<>();
//        models.add(model1);
//        models.add(model2);
//        dataMap.put("models",models);
        return dataMap;
    }

}
