package test.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import test.DocDocument;
import test.service.DocDocumentService;
import test.mapper.DocDocumentMapper;
import org.springframework.stereotype.Service;

/**
* @author 66410
* @description 针对表【doc_document(文档记录表)】的数据库操作Service实现
* @createDate 2022-08-08 10:00:25
*/
@Service
public class DocDocumentServiceImpl extends ServiceImpl<DocDocumentMapper, DocDocument>
    implements DocDocumentService{

}




