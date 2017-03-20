package com.xpleemoon.annotations.demo.annotationprocess.runtime;

import android.support.annotation.IdRes;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于view的运行时注入
 * <ul>
 * <li>编译完成后，在运行时，注解依然存在</li>
 * </ul>
 *
 * @author xpleemoon
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface ViewInject {
    @IdRes int id() default 0;
}
