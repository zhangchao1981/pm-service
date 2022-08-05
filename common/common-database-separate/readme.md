<h1>组件概述</h1>  
实现动态切换mysql数据源，默认数据源为配置文件中配置的数据源，动态切换数据源的方法有两种：<br>

1）查询数据库之前手动编写切换数据源语句
```java
   DataSourceHolder.setDB(databaseName);//databaseName为数据库的名字
```
2）request请求中设置好名为databaseName的attribute，自动切库