package com.xpleemoon.annotation.runtime;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于view的注入
 *
 * @author xpleemoon
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
public @interface InjectView {
    /**
     * view的id，用于IOC时调用{@link android.app.Activity#findViewById(int)}
     *
     * @return
     */
    @IdRes int id() default 0;
}
