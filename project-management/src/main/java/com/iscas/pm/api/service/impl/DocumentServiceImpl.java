package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.iscas.pm.api.mapper.DocumentMapper;
import com.iscas.pm.api.model.project.Document;
import com.iscas.pm.api.service.DocumentService;
import com.iscas.pm.api.util.FastDFSUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

/**
 * @author 66410
 * @description 针对表【document】的数据库操作Service实现
 * @createDate 2022-07-28 18:20:53
 */
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document> implements DocumentService {
    @Autowired
    DocumentMapper documentMapper;
    @Autowired
    FastDFSUtil fastDFSUtil;

    @Override
    public List<Document> getDocuments(Integer directoryId, String documentName) {
        QueryWrapper<Document> documentQueryWrapper = new QueryWrapper<>();
        documentQueryWrapper.eq(null != directoryId, "directory_id", directoryId);
        documentQueryWrapper.like(!StringUtils.isEmpty(documentName), "name", documentName);
        return documentMapper.selectList(documentQueryWrapper);
    }

    @Override
    public Document addLocalDocument(MultipartFile file, Document document) throws IOException {
        if (getDocumentByDirectoryId(document.getDirectoryId()) == null)
            throw new IllegalArgumentException("所属目录不存在");

        if (existSameNameDoc(document.getDirectoryId(),document.getName(),"add") != null)
            throw new IllegalArgumentException("该目录下已存在同名文档");

        //文件存入FastDFs
        StorePath path = fastDFSUtil.upload(file);

        document.setPath(path.getFullPath());
        documentMapper.insert(document);

        return document;
    }

    @Override
    public Document addLinkDocument(Document document) {
        if (getDocumentByDirectoryId(document.getDirectoryId()) == null)
            throw new IllegalArgumentException("所属目录不存在");

        if (existSameNameDoc(document.getDirectoryId(),document.getName(),"add") != null)
            throw new IllegalArgumentException("该目录下已存在同名文档");

        documentMapper.insert(document);
        return document;
    }

    @Override
    public Boolean editDocument(Document document) {
        if (getDocumentByDirectoryId(document.getDirectoryId()) == null)
            throw new IllegalArgumentException("所属目录不存在");

        if (existSameNameDoc(document.getDirectoryId(),document.getName(),"edit") != null)
            throw new IllegalArgumentException("该目录下已存在同名文档");

        documentMapper.updateById(document);
        return null;
    }

    /**
     * 校验目录是否存在
     */
    private List<Document> getDocumentByDirectoryId(Integer directoryId){
        QueryWrapper<Document> documentQueryWrapper = new QueryWrapper<>();
        documentQueryWrapper.eq(null != directoryId, "directory_id", directoryId);
        return documentMapper.selectList(documentQueryWrapper);
    }

    /**
     * 校验同一目录下是否有同名文档
     */
    private List<Document> existSameNameDoc(Integer directoryId,String name,String ops){
        QueryWrapper<Document> documentQueryWrapper = new QueryWrapper<>();
        if ("add".equals(ops)) {
            documentQueryWrapper.eq(null != directoryId, "directory_id", directoryId);
        }else{
            documentQueryWrapper.ne(null != directoryId, "directory_id", directoryId);
        }
        documentQueryWrapper.eq(null != name, "name", name);
        return documentMapper.selectList(documentQueryWrapper);
    }
}




