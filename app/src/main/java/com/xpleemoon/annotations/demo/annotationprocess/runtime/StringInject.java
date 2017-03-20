package com.xpleemoon.annotations.demo.annotationprocess.runtime;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于字符串的运行时注入
 * <ul>
 * <li>编译完成后，在运行时，注解依然存在</li>
 * </ul>
 *
 * @author xpleemoon
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.RUNTIME)
@interface StringInject {
    String value() default "IOC——运行时注解处理\n既要会用洋枪洋炮\n又要会造土枪土炮";
}
