package com.iscas.pm.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.project.Document;

import java.util.List;

/**
* @author 66410
* @description 针对表【document】的数据库操作Service
* @createDate 2022-07-28 18:20:53
*/
public interface DocumentService extends IService<Document> {

    List<Document> getDocument(Integer directoryId, String documentName);
}
