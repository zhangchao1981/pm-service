package com.iscas.pm.api.service.impl.doc;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;

import com.github.tobato.fastdfs.domain.fdfs.StorePath;
import com.iscas.pm.api.mapper.doc.DocTemplateMapper;
import com.iscas.pm.api.model.doc.DocTemplate;
import com.iscas.pm.api.model.doc.param.AddTemplateParam;
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
* @author 66410
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
    public DocTemplate addLocalDocument(AddTemplateParam addTemplateParam) throws IOException {
        DocTemplate docTemplate= new DocTemplate();
        docTemplate.addTemplateParam(addTemplateParam);
        if (docTemplateMapper.selectOne(new QueryWrapper<DocTemplate>().eq("name", addTemplateParam.getName())) != null) {
            throw new IllegalArgumentException("模板名重复");
        }
        docTemplate.setCreateTime(new Date());
        docTemplate.setUpdateTime(new Date());
        docTemplate.setPath(addTemplateParam.getPath());
        docTemplateMapper.insert(docTemplate);
        return docTemplate;
    }

    @Override
    public String uploadTemplate(MultipartFile file) throws IOException {
        //文件存入FastDFs
        StorePath path = fastDFSUtil.upload(file);
        redisUtil.set(path.getFullPath(),null);
        //设置失效时间  (数值待定)
        redisUtil.expire(path.getFullPath(),1000);
        return  path.getFullPath();
    }

    @Override
    public Object downLoadTemplate(String path, String name, HttpServletResponse response) throws IOException {
       fastDFSUtil.download(path,name,response);
        return null;
    }
}




