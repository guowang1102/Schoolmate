package com.weiguowang.schoolmate.utils;

import android.util.Log;

/**
 * function:
 * Created by 韦国旺 on 2017/3/10 0010.
 * Copyright (c) 2017  All Rights Reserved.
 */
public class Logger {

    private Logger(){

    }

    private static  String TAG = "debug";

    public static void getInstance(String tag){
        TAG = tag;
    }


    public void d(String msg){
        Log.d(TAG,msg);
    }





}
