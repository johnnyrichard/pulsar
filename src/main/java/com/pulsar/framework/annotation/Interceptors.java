package com.pulsar.framework.annotation;

import com.pulsar.framework.interceptor.Interceptor;
import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 *
 * @author Johnny Richard
 */
@Retention(RetentionPolicy.RUNTIME)
@Target(ElementType.TYPE)
public @interface Interceptors {
    Class<? extends Interceptor>[] value() default {};
}
