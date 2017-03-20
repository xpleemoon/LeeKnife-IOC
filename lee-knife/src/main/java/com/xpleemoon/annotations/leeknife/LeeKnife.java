package com.xpleemoon.annotations.leeknife;

import android.app.Activity;
import android.support.annotation.NonNull;
import android.view.Window;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;

/**
 * IOC注入工具，使用方式见下面代码：
 * <pre><code>
 * public class ExampleActivity extends Activity {
 *   {@literal @}InjectView(R.id.title) TextView titleView;
 *
 *   {@literal @}Override protected void onCreate(Bundle savedInstanceState) {
 *     super.onCreate(savedInstanceState);
 *     setContentView(R.layout.example_activity);
 *     LeeKnife.inject(this);
 *   }
 * }
 * </code></pre>
 *
 * @author xpleemoon
 */
public final class LeeKnife {
    /**
     * 编译时处理生成的java文件后缀
     */
    private static final String SUFFIX = "$$Injector";
    /**
     * 入口方法名
     */
    private static final String METHOD = "binds";

    private static void check(Activity activity) {
        if (activity == null) {
            throw new IllegalStateException("依赖注入的activity不能为null");
        }

        Window window = activity.getWindow();
        if (window == null || window.getDecorView() == null) {
            throw new IllegalStateException("依赖注入的activity未建立视图");
        }
    }

    /**
     * IOC注入
     *
     * @param target
     */
    public static void inject(@NonNull Activity target) {
        check(target);

        try {
            Class injectorClz = Class.forName(target.getClass().getName() + SUFFIX);
            Method bindsMethod = injectorClz.getMethod(METHOD, target.getClass());
            bindsMethod.invoke(null, target);
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
    }
}
