package com.weiguowang.schoolmate.config;

import android.os.Environment;

import java.io.File;

/**
 * function : 全局设计
 * Created by 韦国旺 on 2017/3/21.
 * Copyright (c) 2017 All Rights Reserved.
 */


public class AppConfig {

    public static final String LOCAL_PATH = Environment.getExternalStorageDirectory() + File.separator + "Schoolmate";

    public static final String HAED_IMG_LOCAL_PATH = LOCAL_PATH + File.separator + "header.jpg";
}
