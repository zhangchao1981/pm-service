package com.energy.common.databaseseparate.mysql;

import com.energy.common.databaseseparate.entity.DynamicInclude;
import com.energy.common.databaseseparate.holder.DataSourceHolder;

import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.springframework.core.annotation.Order;
import org.springframework.stereotype.Component;

@Component
@Aspect
@Order(-10)
@Slf4j
public class DynamicDatasourceAspect {

    @Around("@within(dynamicInclude)")
    public Object filter(ProceedingJoinPoint joinPoint, DynamicInclude dynamicInclude) throws Throwable {
        String curDb = DataSourceHolder.getDB();
        DataSourceHolder.setDB(DataSourceHolder.DEFAULT_DATASOURCE);
        log.info("Datasource is set to[" + curDb + "]");
        try {
            return joinPoint.proceed();
        } finally {
            DataSourceHolder.setDB(curDb);
        }
    }
}
