package com.demo.service.impl;

import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.demo.mapper.DirectoryMapper;
import com.demo.model.project.DocDirectory;
import com.demo.service.DirectoryService;
import org.springframework.stereotype.Service;

/**
* @author 66410
* @description 针对表【doc_directory】的数据库操作Service实现
* @createDate 2022-07-28 18:21:01
*/
@Service
public class DirectoryServiceImpl extends ServiceImpl<DirectoryMapper, DocDirectory>
    implements DirectoryService {

}




