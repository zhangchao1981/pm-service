package com.iscas.pm.api.mapper.doc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.doc.Document;
import com.iscas.pm.api.model.doc.TableByDB;
import com.iscas.pm.api.model.doc.TableStructure;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;

import java.util.List;

/**
 * @author 66410
 * @description 针对表【document】的数据库操作Mapper
 * @createDate 2022-07-28 18:20:53
 * @Entity .model/project.Document
 */
@Mapper
public interface DocumentMapper extends BaseMapper<Document> {


    @Select("select table_name as name,table_comment as comment "
            + "from information_schema.tables "
            + "where table_schema =#{DBName}")
    List<TableByDB> getDBInfo(@Param("DBName") String DBName);


    @Select("SHOW FULL FIELDS " +
            "FROM  ${tableName}")
    List<TableStructure> getTableStructureList(@Param("tableName") String tableName);
}




