package com.iscas.pm.common.db.separate.holder;

public class DataSourceHolder {

    // 默认数据源名称为:default
    public static final String DEFAULT_DATASOURCE = "default";

    private static final  ThreadLocal<String> DATA_SOURCE_HOLDER = new ThreadLocal<>();

    private DataSourceHolder() {
        setDB(DEFAULT_DATASOURCE);
    }

    public static void setDB(String databaseName) {
        DATA_SOURCE_HOLDER.set(databaseName);
    }

    public static String getDB() {
        return DATA_SOURCE_HOLDER.get();
    }

    public static void remove() {
        DATA_SOURCE_HOLDER.remove();
    }

}
