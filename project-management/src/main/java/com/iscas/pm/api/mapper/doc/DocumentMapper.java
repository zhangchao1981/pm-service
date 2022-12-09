package com.iscas.pm.api.mapper.doc;

import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.iscas.pm.api.model.doc.Document;
import com.iscas.pm.api.model.doc.TableInfo;
import com.iscas.pm.api.model.doc.TableFieldInfo;
import org.apache.ibatis.annotations.*;

import java.util.List;

/**
 * @author lichang
 * @description 针对表【document】的数据库操作Mapper
 * @createDate 2022-07-28 18:20:53
 * @Entity .model/project.Document
 */
@Mapper
public interface DocumentMapper extends BaseMapper<Document> {


    @Select("select table_name as name,table_comment as comment "
            + "from information_schema.tables "
            + "where table_schema =#{DBName}")
    List<TableInfo> getAllTables(@Param("DBName") String DBName);


//    @Select("SHOW FULL FIELDS " +
//            "FROM  ${tableName}")
//    @Results({
//            @Result(property = "fieldName", column = "Field"),
//            @Result(property = "fieldComment", column = "Comment"),
//            @Result(property = "fieldType", column = "Type"),
//            @Result(property = "fieldCollation", column = "Collation"),
//            @Result(property = "fieldNull", column = "Null"),
//            @Result(property = "fieldKey", column = "Key"),
//            @Result(property = "fieldDefault", column = "Default"),
//            @Result(property = "fieldExtra", column = "Extra"),
//            @Result(property = "fieldPrivileges", column = "Privileges")}
//    )
@Select(" select TABLE_NAME AS tableName , column_name as fieldName, COLUMN_COMMENT as fieldComment , is_nullable as fieldNull ,data_type as fieldType, column_default as   fieldDefault ,Extra as  fieldExtra ,PRIVILEGES as  fieldPrivileges  ,collation_name as  fieldcollation, column_key as fieldkey" +
        "    from information_schema.columns" +
        "    where table_schema = #{DBName}")
    List<TableFieldInfo> getTableStructureList(@Param("DBName") String DBName);
}




