package com.iscas.pm.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.project.DocDirectory;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 66410
* @description 针对表【doc_directory】的数据库操作Mapper
* @createDate 2022-07-28 18:21:01
* @Entity .model/project.DocDirectory
*/
@Mapper
public interface DocDirectoryMapper extends BaseMapper<DocDirectory> {

}




