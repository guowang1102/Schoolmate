package com.weiguowang.schoolmate;

import android.app.Application;

import com.zhy.autolayout.config.AutoLayoutConifg;

/**
 * function:
 * Created by 韦国旺 on 2017/3/7 0007.
 * Copyright (c) 2017 北京联龙博通 All Rights Reserved.
 */
public class SchoolApp extends Application {
    @Override
    public void onCreate() {
        super.onCreate();
        AutoLayoutConifg.getInstance().useDeviceSize();
    }
}
