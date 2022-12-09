package com.iscas.pm.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.doc.Document;
import com.iscas.pm.api.model.doc.TableInfo;
import com.iscas.pm.api.model.doc.TableFieldInfo;
import com.iscas.pm.api.model.doc.param.CreateDocumentParam;
import com.iscas.pm.api.model.doc.param.DBLinkParam;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.HashMap;
import java.util.List;

/**
* @author 66410
* @description 针对表【document】的数据库操作Service
* @createDate 2022-07-28 18:20:53
*/
public interface DocumentService extends IService<Document> {

    Document addLocalDocument(Document document) ;

    Boolean deleteDocument(Integer id);

    Boolean deleteDocumentBatch(List<Integer> ids);

    void downloadDocument(Integer id, HttpServletResponse response);

    void createDocument(CreateDocumentParam createDocumentParam) throws IOException;

    HashMap<String,Object> getDocumentContext(CreateDocumentParam templateId);

    void deleteTemplate(Integer templateId);

    List<TableInfo> getAllTables(String dbName);

    List<TableFieldInfo> getTableFieldInfoList(String tableName);

    Boolean testDB(DBLinkParam dbLinkParam);
}
