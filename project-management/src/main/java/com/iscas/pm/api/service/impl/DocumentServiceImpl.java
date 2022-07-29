package com.iscas.pm.api.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.iscas.pm.api.mapper.DocumentMapper;
import com.iscas.pm.api.model.project.Document;
import com.iscas.pm.api.service.DocumentService;
import org.springframework.stereotype.Service;

/**
* @author 66410
* @description 针对表【document】的数据库操作Service实现
* @createDate 2022-07-28 18:20:53
*/
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document>
    implements DocumentService {

}




