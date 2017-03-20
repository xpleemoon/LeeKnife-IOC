package com.xpleemoon.annotations.demo.annotationprocess.compile;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.xpleemoon.annotations.InjectString;
import com.xpleemoon.annotations.InjectView;
import com.xpleemoon.annotations.demo.annotationprocess.R;
import com.xpleemoon.annotations.leeknife.LeeKnife;

/**
 * 注意，当前类中使用的注解和{@link com.xpleemoon.annotations.demo.annotationprocess.runtime.RuntimeIOCActivity}的不一致.</br>
 * 当前使用的是module为lee-annotations
 *
 * @author xpleemoon
 */
public class CompileIOCActivity extends AppCompatActivity {
    /**
     * 使用默认值注入
     */
    @InjectString
    String mTextStr;
    @InjectString("编译时注解处理，IOC就这么简单")
    String mToastStr;
    @InjectView(id = R.id.compile_inject_text)
    TextView mText;
    @InjectView(id = R.id.compile_inject_button)
    Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_compile_ioc);

        LeeKnife.inject(this);
        mText.setText(mTextStr);
        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), mToastStr, Toast.LENGTH_LONG).show();
            }
        });
    }

}
