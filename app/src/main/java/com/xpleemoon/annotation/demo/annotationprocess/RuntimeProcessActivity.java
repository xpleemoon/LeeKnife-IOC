package com.xpleemoon.annotation.demo.annotationprocess;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;

import com.xpleemoon.annotation.runtime.InjectString;
import com.xpleemoon.annotation.runtime.InjectView;
import com.xpleemoon.annotation.runtime.XInjectUtils;

public class RuntimeProcessActivity extends AppCompatActivity {
    /**
     * 使用默认值注入
     */
    @InjectString
    private String mStr;
    @InjectView(id = R.id.runtime_inject_button)
    private Button mBtn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_runtime_process);

        XInjectUtils.inject(this); // 运行时注入，切记在使用注解修饰的字段之前调用，否则会因为未注入而造成NullPointerException

        mBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(getApplicationContext(), mStr, Toast.LENGTH_LONG).show();
            }
        });
    }
}
