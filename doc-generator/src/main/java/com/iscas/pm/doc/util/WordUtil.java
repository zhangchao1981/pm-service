package com.iscas.pm.doc.util;

import com.iscas.pm.doc.model.Case;
import com.iscas.pm.doc.model.Model;
import org.bouncycastle.math.raw.Mod;

import java.io.UnsupportedEncodingException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author by zhangchao
 * @date 2022/6/16.
 * word文档工具类
 */
public class WordUtil {
    public static void main(String[] args) throws UnsupportedEncodingException {
        DocumentHandler documentHandler = new DocumentHandler();
        documentHandler.createDoc(getData(),"F:/test.doc");
    }

    private static Map<String,Object> getData(){
        Map<String,Object> dataMap = new HashMap<>();
        dataMap.put("wendangbiaoshi","文档标识001");
        dataMap.put("gaishu","文档概述文档概述文档概述文档概述文档概述文档概述文档概述文档概述文档概述文档概述文档概述文档概述文档概述文档概述文档概述文档概述文档概述文档概述");
        Case case1 = new Case("0001","用例1");
        Case case2 = new Case("0002","用例2");
        List<Case> caseList = new ArrayList<>();
        caseList.add(case1);
        caseList.add(case2);
        Model subModel = new Model("子模块1",caseList,new ArrayList<>());
        List<Model> subModels = new ArrayList<>();
        subModels.add(subModel);
        Model model1 = new Model("模块1",new ArrayList<>(),subModels);
        Model model2 = new Model("模块2",caseList,new ArrayList<>());

        List<Model> models = new ArrayList<>();
        models.add(model1);
        models.add(model2);
        dataMap.put("models",models);

        return dataMap;
    }

}
