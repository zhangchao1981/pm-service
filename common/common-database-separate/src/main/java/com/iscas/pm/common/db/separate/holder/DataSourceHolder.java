package com.iscas.pm.common.db.separate.holder;

import org.apache.commons.lang.StringUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.util.ObjectUtils;

/**
 * @Author： zhangchao
 * @Date： 2022/7/15
 * @Description： ThreadLocal变量，存放当前数据源
 */
public class DataSourceHolder {
    @Autowired
    private Environment environment;

    // 默认数据源名称为:default
    //把数据库的全部配置都放到PRIVATE_DATA_SOURCE_HOLDER里，除了数据库名，如果改变别的配置，就用改变了的配置，如果没变就沿用默认配置
    public static final String DEFAULT_DATASOURCE = "default";

//    private static final  ThreadLocal<String> DATA_SOURCE_HOLDER = new ThreadLocal<>();

    private static final  ThreadLocal<DBInfo> PRIVATE_DATA_SOURCE_HOLDER = new ThreadLocal<>();

    private DataSourceHolder() {
        setDB(DEFAULT_DATASOURCE);
    }

    public static void setDB(String databaseName) {
//        DBInfo dbInfo = PRIVATE_DATA_SOURCE_HOLDER.get();
        PRIVATE_DATA_SOURCE_HOLDER.set(new DBInfo().setDatabaseName(databaseName));
    }

    public static void setDB(String url,String databaseName, String userName,String password, String driverClassName) {
        DBInfo dbInfo = new DBInfo(url,databaseName,userName,password,driverClassName);
        PRIVATE_DATA_SOURCE_HOLDER.set(dbInfo);  //配置信息
    }

    public static DBInfo getDB() {
        if(ObjectUtils.isEmpty(PRIVATE_DATA_SOURCE_HOLDER.get())||StringUtils.isBlank(PRIVATE_DATA_SOURCE_HOLDER.get().databaseName)){
            setDB(DEFAULT_DATASOURCE);
        }
        return PRIVATE_DATA_SOURCE_HOLDER.get();
    }

    public static void remove() {
        PRIVATE_DATA_SOURCE_HOLDER.remove();
    }

}
