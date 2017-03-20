package com.xpleemoon.compiler;

import com.google.auto.service.AutoService;
import com.squareup.javapoet.JavaFile;
import com.squareup.javapoet.MethodSpec;
import com.squareup.javapoet.TypeName;
import com.squareup.javapoet.TypeSpec;
import com.xpleemoon.annotations.InjectString;
import com.xpleemoon.annotations.InjectView;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;

import javax.annotation.processing.AbstractProcessor;
import javax.annotation.processing.Filer;
import javax.annotation.processing.Messager;
import javax.annotation.processing.ProcessingEnvironment;
import javax.annotation.processing.Processor;
import javax.annotation.processing.RoundEnvironment;
import javax.lang.model.SourceVersion;
import javax.lang.model.element.Element;
import javax.lang.model.element.ElementKind;
import javax.lang.model.element.Modifier;
import javax.lang.model.element.TypeElement;
import javax.lang.model.util.Elements;
import javax.tools.Diagnostic;

@AutoService(Processor.class)
public class LeeProcessor extends AbstractProcessor {
    /**
     * 编译时处理生成的java文件后缀
     */
    private static final String SUFFIX = "$$Injector";
    /**
     * 入口方法名
     */
    private static final String METHOD = "binds";
    /**
     * 入口方法参数
     */
    private static final String PARAMETER = "target";
    /**
     * key：类的全限定名；value：对应类中所有注解的集合
     */
    private final Map<String, Set<AbstractBinding>> mBindingMap = new HashMap<>();
    /**
     * 可用于编译时，信息打印
     */
    private Messager mMessager;
    /**
     * 用语{@link Element}处理的工具类
     */
    private Elements mElementUtils;
    /**
     * 用语文件创建
     */
    private Filer mFiler;

    @Override
    public synchronized void init(ProcessingEnvironment processingEnvironment) {
        super.init(processingEnvironment);
        this.mMessager = processingEnvironment.getMessager();
        this.mElementUtils = processingEnvironment.getElementUtils();
        this.mFiler = processingEnvironment.getFiler();
    }

    @Override
    public Set<String> getSupportedAnnotationTypes() {
        Set<String> annotations = new LinkedHashSet<>();
        annotations.add(InjectString.class.getCanonicalName());
        annotations.add(InjectView.class.getCanonicalName());
        return annotations;
    }

    @Override
    public SourceVersion getSupportedSourceVersion() {
        return SourceVersion.latestSupported();
    }

    @Override
    public boolean process(Set<? extends TypeElement> set, RoundEnvironment roundEnvironment) {
        parseInjector(InjectString.class, roundEnvironment.getElementsAnnotatedWith(InjectString.class));
        parseInjector(InjectView.class, roundEnvironment.getElementsAnnotatedWith(InjectView.class));

        generateCode();
        return false;
    }

    /**
     * 解析注解
     *
     * @param injectorClz
     * @param injectorElements
     */
    private void parseInjector(Class<?> injectorClz, Set<? extends Element> injectorElements) {
        if (injectorClz == null
                || injectorElements == null
                || injectorElements.size() <= 0) {
            return;
        }

        mMessager.printMessage(Diagnostic.Kind.NOTE, "解析注解：" + injectorClz.getSimpleName());

        Map<String, List<Element>> map = new HashMap<>();
        for (Element element : injectorElements) {
            if (element.getKind() != ElementKind.FIELD) {
                String exceptionMsg = "Only fields can be annotated with " + injectorClz.getSimpleName();
                mMessager.printMessage(Diagnostic.Kind.ERROR, exceptionMsg, element);
                throw new IllegalStateException(exceptionMsg);
            }

            String fullClassName = TypeInfoUtils.getFullClzName(mElementUtils, element);
            List<Element> elementList = map.get(fullClassName);
            if (elementList == null) {
                elementList = new ArrayList<>();
                map.put(fullClassName, elementList);
            }
            elementList.add(element);
        }

        for (Map.Entry<String, List<Element>> entry : map.entrySet()) {
            String fullClassName = entry.getKey();
            Set<AbstractBinding> bindings = mBindingMap.get(fullClassName);
            if (bindings == null) {
                bindings = new HashSet<>();
                mBindingMap.put(fullClassName, bindings);
            }

            if (injectorClz == InjectString.class) {
                bindings.add(new StringBinding(InjectString.class, mElementUtils, mMessager, entry.getValue()));
            } else if (injectorClz == InjectView.class) {
                bindings.add(new ViewBinding(InjectView.class, mElementUtils, mMessager, entry.getValue()));
            }
        }
    }

    /**
     * 生成代码
     */
    private void generateCode() {
        for (Map.Entry<String, Set<AbstractBinding>> entry : mBindingMap.entrySet()) {
            Set<AbstractBinding> bindings = entry.getValue();
            if (bindings == null || bindings.size() <= 0) {
                continue;
            }
            List<Element> elementList = bindings.iterator().next().mElementList;
            if (elementList == null || elementList.size() <= 0) {
                continue;
            }

            Element element = elementList.get(0);
            String simpleClzName = TypeInfoUtils.getSimpleClzName(element) + SUFFIX; // 生成java类的simple名
            TypeName typeName = TypeInfoUtils.getEnclosingTypeName(element); // 注解的宿主类

            // 创建一系列方法
            mMessager.printMessage(Diagnostic.Kind.NOTE, "创建入口方法：" + METHOD);
            MethodSpec.Builder bindsMethodBuilder = MethodSpec.methodBuilder(METHOD)
                    .addModifiers(Modifier.PUBLIC, Modifier.STATIC)
                    .returns(void.class)
                    .addParameter(typeName, PARAMETER)
                    .addJavadoc("数据绑定的入口\n为{@code $N}中{@link $T}和{@link $T}注解的变量绑定数据", PARAMETER, InjectString.class, InjectView.class);
            List<MethodSpec> methodSpecs = new ArrayList<>(); // 方法列表，用于后续创建类时使用
            for (AbstractBinding binding : bindings) {
                methodSpecs.add(binding.generateMethod());
                bindsMethodBuilder.addStatement(binding.getMethodName() + "($N)", PARAMETER);
            }
            methodSpecs.add(bindsMethodBuilder.build());

            // 创建java类
            mMessager.printMessage(Diagnostic.Kind.NOTE, "创建类：" + simpleClzName);
            TypeSpec.Builder injectorClzBuilder = TypeSpec.classBuilder(simpleClzName)
                    .addModifiers(Modifier.PUBLIC, Modifier.FINAL)
                    .addJavadoc("{@link $T}的leeknife注解字段注入器\n" +
                            "<ul><li>通过编译器挂载的注解处理器自动生成java源文件后，编译器会再将该java源文文件同其它java源文件一起编译成class文件</li></ul>\n\n" +
                            "@author $N", typeName, LeeProcessor.class.getSimpleName());
            for (MethodSpec methodSpec : methodSpecs) {
                injectorClzBuilder.addMethod(methodSpec);
            }
            TypeSpec injectorClz = injectorClzBuilder.build();

            // 生成java源文件
            mMessager.printMessage(Diagnostic.Kind.NOTE, "创建java源文件：" + simpleClzName);
            JavaFile javaFile = JavaFile.builder(TypeInfoUtils.getPkgName(mElementUtils, element), injectorClz)
                    .addFileComment("This codes are generated automatically. Do not modify!")
                    .build();
            try {
                javaFile.writeTo(mFiler);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
