package com.doc.mapper;
import java.util.List;
import org.apache.ibatis.annotations.Param;

import com.doc.domain.Pjdata;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;

/**
* @author 66410
* @description 针对表【pjdata】的数据库操作Mapper
* @createDate 2022-07-04 13:44:25
* @Entity com.doc.domain.Pjdata
*/
public interface PjdataMapper extends BaseMapper<Pjdata> {
    List<Pjdata> selectAllById(@Param("id") Integer id);
}




