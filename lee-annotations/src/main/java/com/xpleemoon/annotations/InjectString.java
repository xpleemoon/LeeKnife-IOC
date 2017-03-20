package com.xpleemoon.annotations;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * 用于字符串的编译时注入
 * <ul>
 * <li>编译完成后，注解失效</li>
 * </ul>
 *
 * @author xpleemoon
 */
@Target(ElementType.FIELD)
@Retention(RetentionPolicy.CLASS)
public @interface InjectString {
    String value() default "IOC——编译时注解处理\n既要会用洋枪洋炮\n又要会造土枪土炮";
}
