package com.xpleemoon.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于view的编译时注入
 * <ul>
 * <li>编译完成后，注解失效</li>
 * </ul>
 *
 * @author xpleemoon
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface InjectView {
    int id() default 0;
}
