package com.energy.common.databaseseparate.holder;


public class DataSourceHolder {
    /**
     * 默认数据源
     */
    public static final String DEFAULT_USER = "Authorization_1";
    public static final String DEFAULT_DATASOURCE = "default";
    public static final String DEFAULT_SPLIT = "_";
    public static final String DEFAULT_SYSTEM_ID = "1";
//ThreadLocal 适用于每个线程需要自己独立的实例且该实例需要在多个方法中被使用，也即变量在线程间隔离而在方法或类间共享的场景
    private static final  ThreadLocal<String> DATA_SOURCE_HOLDER = new ThreadLocal<>();

    private DataSourceHolder() {
        setDB(DEFAULT_DATASOURCE);
    }

    public static void setDB(String databaseName) {
        DATA_SOURCE_HOLDER.set(databaseName);//设置当前线程的DATA_SOURCE_HOLDER属性为databaseName的值
    }

    public static String getDB() {
        return DATA_SOURCE_HOLDER.get();
    }

    public static void remove() {
        DATA_SOURCE_HOLDER.remove();
    }

}
