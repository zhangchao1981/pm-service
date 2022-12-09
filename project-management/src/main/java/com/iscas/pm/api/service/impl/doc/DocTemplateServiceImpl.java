package com.iscas.pm.api.service.impl.doc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.iscas.pm.api.mapper.doc.DocTemplateMapper;
import com.iscas.pm.api.model.doc.DocTemplate;
import com.iscas.pm.api.service.DocTemplateService;
import com.iscas.pm.api.util.FastDFSUtil;
import com.iscas.pm.common.core.util.RedisUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import javax.servlet.http.HttpServletResponse;
import java.io.IOException;
import java.util.Date;

/**
* @author lichang
* @description 针对表【template】的数据库操作Service实现
* @createDate 2022-07-28 18:18:33
*/
@Service
public class DocTemplateServiceImpl extends ServiceImpl<DocTemplateMapper, DocTemplate> implements DocTemplateService {
    @Autowired
    FastDFSUtil fastDFSUtil;
    @Autowired
    RedisUtil redisUtil;
    @Autowired
    DocTemplateMapper docTemplateMapper;

    @Override
    public DocTemplate uploadLocalTemplate(DocTemplate docTemplate) {
        if (docTemplateMapper.selectOne(new QueryWrapper<DocTemplate>().eq("name", docTemplate.getName())) != null) {
            throw new IllegalArgumentException("模板名称不能重复"); }

        docTemplate.setCreateTime(new Date());
        docTemplate.setUpdateTime(new Date());
        docTemplateMapper.insert(docTemplate);
        return docTemplate;
    }

    @Override
    public String uploadTemplate(MultipartFile file) throws IOException {
        //文件存入FastDFs
        StorePath path = fastDFSUtil.upload(file);
        return  path.getFullPath();
    }

    @Override
    public Object downLoadTemplate(String path, String name, HttpServletResponse response) throws IOException {
       fastDFSUtil.download(path,name,response);
        return null;
    }
}




