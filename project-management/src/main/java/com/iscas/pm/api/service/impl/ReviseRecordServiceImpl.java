package .service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import .model/project.ReviseRecord;
import .service.ReviseRecordService;
import .mapper.ReviseRecordMapper;
import org.springframework.stereotype.Service;

/**
* @author 66410
* @description 针对表【revise_record】的数据库操作Service实现
* @createDate 2022-07-28 18:18:16
*/
@Service
public class ReviseRecordServiceImpl extends ServiceImpl<ReviseRecordMapper, ReviseRecord>
    implements ReviseRecordService{

}




