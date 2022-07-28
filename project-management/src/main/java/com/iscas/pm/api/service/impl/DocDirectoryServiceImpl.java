package .service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import .model/project.DocDirectory;
import .service.DocDirectoryService;
import .mapper.DocDirectoryMapper;
import org.springframework.stereotype.Service;

/**
* @author 66410
* @description 针对表【doc_directory】的数据库操作Service实现
* @createDate 2022-07-28 18:21:01
*/
@Service
public class DocDirectoryServiceImpl extends ServiceImpl<DocDirectoryMapper, DocDirectory>
    implements DocDirectoryService{

}




