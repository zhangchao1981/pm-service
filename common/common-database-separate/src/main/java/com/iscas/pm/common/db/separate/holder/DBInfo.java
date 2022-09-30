package com.iscas.pm.common.db.separate.holder;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @Author： zhangchao
 * @Date： 2022/9/28
 * @Description：
 */
@Data
@Accessors(chain = true)
public class DBInfo {

    public String UserName;
    public String password;
    public String driverClassName;
    public String url;
    public String databaseName;
    public String dataSourceName;

    public DBInfo() {
    }

    public DBInfo(String url, String databaseName, String userName, String password, String driverClassName, String dataSourceName) {
        this.UserName = userName;
        this.password = password;
        this.url = url;
        this.databaseName = databaseName;
        this.driverClassName = driverClassName;
        this.dataSourceName = dataSourceName;
    }
}
