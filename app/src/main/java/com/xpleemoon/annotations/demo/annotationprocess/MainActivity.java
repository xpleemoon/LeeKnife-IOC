package com.xpleemoon.annotations.demo.annotationprocess;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import com.xpleemoon.annotations.demo.annotationprocess.runtime.RuntimeProcessActivity;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void onClickRuntime(View view) {
        startActivity(new Intent(this, RuntimeProcessActivity.class));
    }
}
