package .service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import .model/project.Document;
import .service.DocumentService;
import .mapper.DocumentMapper;
import org.springframework.stereotype.Service;

/**
* @author 66410
* @description 针对表【document】的数据库操作Service实现
* @createDate 2022-07-28 18:20:53
*/
@Service
public class DocumentServiceImpl extends ServiceImpl<DocumentMapper, Document>
    implements DocumentService{

}




