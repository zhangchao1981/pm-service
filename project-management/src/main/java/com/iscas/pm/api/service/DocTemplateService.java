package com.iscas.pm.api.service;

import com.baomidou.mybatisplus.extension.service.IService;
import com.iscas.pm.api.model.doc.DocTemplate;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletResponse;
import java.io.IOException;

/**
* @author 66410
* @description 针对表【template】的数据库操作Service
* @createDate 2022-07-28 18:18:33
*/
public interface DocTemplateService extends IService<DocTemplate> {


    DocTemplate uploadLocalTemplate(DocTemplate addTemplateParam) throws IOException;

    String uploadTemplate(MultipartFile file) throws IOException;

    Object downLoadTemplate(String path, String name, HttpServletResponse response) throws IOException;
}
