package com.iscas.pm.api.mapper;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.project.Document;
import org.apache.ibatis.annotations.Mapper;

/**
* @author 66410
* @description 针对表【document】的数据库操作Mapper
* @createDate 2022-07-28 18:20:53
* @Entity .model/project.Document
*/
@Mapper
public interface DocumentMapper extends BaseMapper<Document> {

}




