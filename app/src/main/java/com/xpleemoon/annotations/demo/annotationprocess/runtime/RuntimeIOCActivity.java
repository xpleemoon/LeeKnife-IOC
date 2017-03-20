package com.xpleemoon.annotations.demo.annotationprocess.runtime;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xpleemoon.annotations.InjectString;
import com.xpleemoon.annotations.demo.annotationprocess.R;

/**
 * 注意，当前类中使用的注解和{@link com.xpleemoon.annotations.demo.annotationprocess.compile.CompileIOCActivity}的不一致.</br>
 * 当前使用的是runtime包下的
 *
 * @author xpleemoon
 */
public class RuntimeIOCActivity extends AppCompatActivity {
    /**
     * 使用默认值注入
     */
    @StringInject
    String mTextStr;
    @StringInject("运行时注解处理，IOC就这么简单")
    String mToastStr;
    @ViewInject(id = R.id.runtime_inject_text)
    private TextView mText;
    @ViewInject(id = R.id.runtime_inject_button)
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_ioc);

        InjectUtils.inject(this);
        mText.setText(mTextStr);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), mToastStr, Toast.LENGTH_LONG).show();
            }
        });
    }
}
