package com.energy.common.databaseseparate.mysql;

import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

//@Mapper
//@ConditionalOnBean(SqlSessionFactoryBean.class)
public interface DatabaseMapper {
    @Update("create database ${name};")
    public void createNewDatabase(@Param(value = "name") String name);

    @Update("use ${databasename};${sql};")
    public void createTables(@Param("databasename") String databasename , @Param("sql") String sql);

    @Update("drop database ${databasename};")
    public  void dropDatabase(@Param("databasename") String databasename);

}
