package com.xpleemoon.compiler;

import com.squareup.javapoet.MethodSpec;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

/**
 * 用于生成注解T相应的处理方法{@link #getMethodName()}
 *
 * @param <T> 注解类型
 * @author xpleemoon
 */
abstract class AbstractBinding<T> {
    protected final Class<T> mClz;
    protected final List<Element> mElementList;
    protected final Elements mElementUtils;
    protected final Messager mMessager;

    AbstractBinding(Class<T> clz, Elements elementUtils, Messager messager, List<Element> elementList) {
        this.mClz = clz;
        this.mElementUtils = elementUtils;
        this.mMessager = messager;
        this.mElementList = elementList;
    }

    /**
     * 获取生成数据绑定的方法名
     *
     * @return
     */
    abstract String getMethodName();

    /**
     * 生成绑定方法
     *
     * @return
     */
    MethodSpec generateMethod() {
        mMessager.printMessage(Diagnostic.Kind.NOTE, "创建方法：" + getMethodName());
        return null;
    }
}
