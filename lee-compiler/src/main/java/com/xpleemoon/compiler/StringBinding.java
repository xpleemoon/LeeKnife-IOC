package com.xpleemoon.compiler;

import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.xpleemoon.annotations.InjectString;

import java.util.List;

import javax.annotation.processing.Messager;
import javax.lang.model.element.Element;
import javax.lang.model.element.Modifier;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

final class StringBinding extends AbstractBinding<InjectString> {

    StringBinding(Class<InjectString> clz, Elements elementUtils, Messager messager, List<Element> elementList) {
        super(clz, elementUtils, messager, elementList);
    }

    @Override
    String getMethodName() {
        return "bindString";
    }

    @Override
    MethodSpec generateMethod() {
        super.generateMethod();

        TypeName enclosingTypeName = TypeInfoUtils.getEnclosingTypeName(mElementList.get(0)); // 获取注解宿主类
        MethodSpec.Builder builder = MethodSpec.methodBuilder(getMethodName())
                .addModifiers(Modifier.PRIVATE, Modifier.STATIC)
                .returns(void.class)
                .addParameter(enclosingTypeName, "target")
                .addJavadoc("为{@code $N}中{@link $T}注解的字符串变量绑定数据", "target", mClz);
        for (Element stringInjectorElement : mElementList) {
            builder.addStatement("target.$N = $S", stringInjectorElement.getSimpleName(), stringInjectorElement.getAnnotation(mClz).value());
        }

        return builder.build();
    }
}
