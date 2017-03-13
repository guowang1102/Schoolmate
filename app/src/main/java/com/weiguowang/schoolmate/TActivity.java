package com.weiguowang.schoolmate;

import android.view.View;

import com.weiguowang.schoolmate.utils.Toasty;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * function:
 * Created by 韦国旺 on 2017/3/7 0007.
 * Copyright (c) 2017  All Rights Reserved.
 */
public abstract class TActivity extends AutoLayoutActivity {

    /**
     * toasty message
     * @param msg
     */
    protected void toastyInfo(String msg){
        Toasty.info(this,msg).show();
    }







}
