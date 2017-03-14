package com.xpleemoon.annotation.runtime;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * IOC工具类
 *
 * @author xpleemoon
 */
public final class XInjectUtils {
    private static void check(Activity activity) {
        if (activity == null) {
            throw new IllegalStateException("依赖注入的activity不能为null");
        }

        Window window = activity.getWindow();
        if (window == null || window.getDecorView() == null) {
            throw new IllegalStateException("依赖注入的activity未建立视图");
        }
    }

    public static void inject(@NonNull Activity activity) {
        check(activity);

        Class<? extends Activity> actClass = activity.getClass();
        Field[] fields = actClass.getDeclaredFields(); // 获取activity中的所有字段
        for (Field field : fields) {
            InjectString injectString = field.getAnnotation(InjectString.class);
            if (injectString != null) {
                String str = injectString.value();
                if (!TextUtils.isEmpty(str)) {
                    try {
                        field.setAccessible(true);
                        field.set(activity, str); // 为InjectString修饰的字段注入字符串
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                continue;
            }

            InjectView injectView = field.getAnnotation(InjectView.class);
            if (injectView != null) {
                int viewId = injectView.id();
                if (viewId > 0) {
                    try {
                        Method findViewByIdMethod = actClass.getMethod("findViewById", int.class);
                        View view = (View) findViewByIdMethod.invoke(activity, viewId); // 反射调用，获取view对象
                        field.setAccessible(true);
                        field.set(activity, view); // 为InjectView修饰的字段注入view
                    } catch (NoSuchMethodException e) {
                        e.printStackTrace();
                    } catch (InvocationTargetException e) {
                        e.printStackTrace();
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                continue;
            }
        }
    }
}
