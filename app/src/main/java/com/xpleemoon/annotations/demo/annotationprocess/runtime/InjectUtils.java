package com.xpleemoon.annotations.demo.annotationprocess.runtime;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.text.TextUtils;
import android.view.View;
import android.view.Window;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * 运行时IOC注入工具，使用方式见下面代码：
 * <pre><code>
 * public class ExampleActivity extends Activity {
 *   {@literal @}ViewInject(R.id.title) TextView titleView;
 *
 *   {@literal @}Override protected void onCreate(Bundle savedInstanceState) {
 *     super.onCreate(savedInstanceState);
 *     setContentView(R.layout.example_activity);
 *     InjectUtils.inject(this);
 *   }
 * }
 * </code></pre>
 *
 * @author xpleemoon
 */
final class InjectUtils {
    private static void check(Activity activity) {
        if (activity == null) {
            throw new IllegalStateException("依赖注入的activity不能为null");
        }

        Window window = activity.getWindow();
        if (window == null || window.getDecorView() == null) {
            throw new IllegalStateException("依赖注入的activity未建立视图");
        }
    }

    public static void inject(@NonNull Activity target) {
        check(target);

        Class<? extends Activity> targetClz = target.getClass();
        Field[] fields = targetClz.getDeclaredFields(); // 获取target中的所有字段
        for (Field field : fields) {
            StringInject stringInject = field.getAnnotation(StringInject.class);
            if (stringInject != null) {
                String str = stringInject.value();
                if (!TextUtils.isEmpty(str)) {
                    try {
                        field.setAccessible(true);
                        field.set(target, str); // 为StringInject修饰的字段注入字符串
                    } catch (IllegalAccessException e) {
                        e.printStackTrace();
                    }
                }
                continue;
            }

            ViewInject viewInject = field.getAnnotation(ViewInject.class);
            if (viewInject != null) {
                int viewId = viewInject.id();
                if (viewId > 0) {
                    try {
                        Method findViewByIdMethod = targetClz.getMethod("findViewById", int.class);
                        View view = (View) findViewByIdMethod.invoke(target, viewId); // 反射调用，获取view对象
                        field.setAccessible(true);
                        field.set(target, view); // 为ViewInject修饰的字段注入view
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
