package com.weiguowang.schoolmate.activity;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.fragment.LoadFragment;


/**
 * function:
 * Created by 韦国旺 on 2017/4/1 0001.
 * Copyright (c) 2017 北京联龙博通 All Rights Reserved.
 */
public class TestActivity extends TActivity {
    private LoadFragment loadFragment;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        loadFragment = LoadFragment.newInstance();
    }

    public void onClick(View view) {
//        ProgressWheelDialog.getInstance(this).show();

        loadFragment.show(getSupportFragmentManager(), "");
        h.sendEmptyMessageDelayed(0, 5000);
    }

    private Handler h = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
//            ProgressWheelDialog.getInstance(TestActivity.this).dismiss();
            loadFragment.dismiss();
        }
    };
}
