package com.iscas.pm.common.core.util;

import java.lang.annotation.*;

@Target({ElementType.TYPE, ElementType.METHOD, ElementType.PACKAGE})
@Retention(RetentionPolicy.RUNTIME)
@Inherited
@Documented
public @interface Wrapper {
}