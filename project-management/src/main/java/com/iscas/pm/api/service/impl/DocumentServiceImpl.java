package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.DocumentMapper;
import com.iscas.pm.api.model.project.Document;
import com.iscas.pm.api.service.DocumentService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;

import java.util.List;

/**
 * @author 66410
 * @description 针对表【document】的数据库操作Service实现
 * @createDate 2022-07-28 18:20:53
 */
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document>
        implements DocumentService {
    @Autowired
    DocumentMapper documentMapper;

    @Override
    public List<Document> getDocument(Integer directoryId, String documentName) {
        //根据外键：目录 查询   and  文档名称
        QueryWrapper<Document> documentQueryWrapper = new QueryWrapper<>();
        documentQueryWrapper.eq(null != directoryId, "directory_id", directoryId);
        documentQueryWrapper.like(!StringUtils.isEmpty(documentName), "name", documentName);
        return documentMapper.selectList(documentQueryWrapper);
    }
}




