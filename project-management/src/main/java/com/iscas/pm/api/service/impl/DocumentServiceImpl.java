package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.iscas.pm.api.mapper.doc.DocumentMapper;
import com.iscas.pm.api.model.doc.Document;
import com.iscas.pm.api.model.doc.DocumentTypeEnum;
import com.iscas.pm.api.service.DocumentService;
import com.iscas.pm.api.util.FastDFSUtil;
import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
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
    public Document addLocalDocument(MultipartFile file, Document document) throws IOException {
        if (getDocumentByDirectoryId(document.getDirectoryId()) == null)
            throw new IllegalArgumentException("所属目录不存在");

        if (existSameNameDoc(document.getDirectoryId(), document.getName(), "add"))
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

        if (existSameNameDoc(document.getDirectoryId(), document.getName(), "add"))
            throw new IllegalArgumentException("该目录下已存在同名文档");

        documentMapper.insert(document);
        return document;
    }

    @Override
    public Boolean deleteDocument(Integer id) {
        Document document = documentMapper.selectById(id);
        if (document == null)
            throw new IllegalArgumentException("待删除的文档不存在");

        if (StringUtils.isNotBlank(document.getPath()) && document.getType() != DocumentTypeEnum.LINK)
            fastDFSUtil.delete(document.getPath());
        return true;
    }

    @Override
    public Boolean deleteDocumentBatch(List<Integer> ids) {
        ids.forEach(id -> {
            deleteDocument(id);
        });
        return null;
    }

    @Override
    public void downloadDocument(Integer id, HttpServletResponse response) {
        Document document = documentMapper.selectById(id);
        if (document == null)
            throw new IllegalArgumentException("待下载的文档不存在");

        if (StringUtils.isNotBlank(document.getPath()) && document.getType() != DocumentTypeEnum.LINK) {
            try {
                fastDFSUtil.download(document.getPath(),document.getName(),response);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * 校验目录是否存在
     */
    private List<Document> getDocumentByDirectoryId(Integer directoryId) {
        return documentMapper.selectList(new QueryWrapper<Document>().eq(null != directoryId, "directory_id", directoryId));
    }

    /**
     * 校验同一目录下是否有同名文档
     */
    private Boolean existSameNameDoc(Integer directoryId, String name, String ops) {
        QueryWrapper<Document> documentQueryWrapper = new QueryWrapper<>();
        if ("add".equals(ops)) {
            documentQueryWrapper.eq(null != directoryId, "directory_id", directoryId);
        } else {
            documentQueryWrapper.ne(null != directoryId, "directory_id", directoryId);
        }
        documentQueryWrapper.eq(null != name, "name", name);

        List<Document> documents = documentMapper.selectList(documentQueryWrapper);
        if (documents == null || documents.size() == 0)
            return false;
        return true;
    }
}




