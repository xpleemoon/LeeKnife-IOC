package com.xpleemoon.compiler;

import com.squareup.javapoet.TypeName;

import javax.lang.model.element.Element;
import javax.lang.model.type.TypeMirror;
import javax.lang.model.util.Elements;

/**
 * 类型信息工具类
 *
 * @author xpleemoon
 */
final class TypeInfoUtils {
    static String getPkgName(Elements elementUtils, Element element) {
        return elementUtils.getPackageOf(element).getQualifiedName().toString();
    }

    static String getSimpleClzName(Element element) {
        return element.getEnclosingElement().getSimpleName().toString();
    }

    static String getFullClzName(Elements elementUtils, Element element) {
        return getPkgName(elementUtils, element) + "." + getSimpleClzName(element);
    }

    static TypeName getTypeName(Element element) {
        TypeMirror typeMirror = element.asType();
        return TypeName.get(typeMirror);
    }

    static TypeName getEnclosingTypeName(Element element) {
        TypeMirror elementType = element.getEnclosingElement().asType();
        return TypeName.get(elementType);
    }
}
