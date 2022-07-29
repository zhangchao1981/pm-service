package com.iscas.pm.api.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Update;

@Mapper
public interface InitializationMapper {
    @Update("create database if not exists ${databaseName} default character set utf8mb4 collate utf8mb4_unicode_ci")
    boolean createDatabase(@Param("databaseName") String databaseName);

    @Update("drop database if exists ${databaseName}")
    boolean dropDatabase(@Param("databaseName") String databaseName);
}
