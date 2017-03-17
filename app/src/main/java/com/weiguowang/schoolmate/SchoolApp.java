package com.weiguowang.schoolmate;

import android.app.Application;

import com.zhy.autolayout.config.AutoLayoutConifg;

import cn.bmob.v3.Bmob;

/**
 * function:
 * Created by 韦国旺 on 2017/3/7 0007.
 * Copyright (c) 2017 All Rights Reserved.
 */
public class SchoolApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        init();

    }

    private void init() {
        AutoLayoutConifg.getInstance().useDeviceSize();
        Bmob.initialize(this, "cc81cd3258bb6826a6fe11aa00821a06");
    }
}
