package com.energy.common.databaseseparate.entity;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

public interface Dynamic {
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Documented
    @interface include{

    }
    @Retention(RetentionPolicy.RUNTIME)
    @Inherited
    @Target({ElementType.METHOD, ElementType.TYPE})
    @Documented
    @interface exclude{

    }
}
