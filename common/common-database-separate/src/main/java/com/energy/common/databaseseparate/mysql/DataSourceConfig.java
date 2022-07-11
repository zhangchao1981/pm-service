package com.energy.common.databaseseparate.mysql;

import com.alibaba.druid.pool.DruidDataSource;
import lombok.Data;

@Data
public class DataSourceConfig {
    private DruidDataSource druidDataSource;
    private String dataSourceName;

    public DataSourceConfig(){
        druidDataSource = new DruidDataSource();
    }

    public void setUrl(String url){
        druidDataSource.setUrl(url);
    }

    public void setUsername(String username){
        druidDataSource.setUsername(username);
    }

    public void setPassword(String password){
        druidDataSource.setPassword(password);
    }

    public void setDriveClassName(String driveClassName){
        druidDataSource.setDriverClassName(driveClassName);
    }
}
