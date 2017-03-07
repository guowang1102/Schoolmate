package com.weiguowang.schoolmate;

import com.weiguowang.schoolmate.utils.Toasty;
import com.zhy.autolayout.AutoLayoutActivity;

/**
 * function:
 * Created by 韦国旺 on 2017/3/7 0007.
 * Copyright (c) 2017 北京联龙博通 All Rights Reserved.
 */
public abstract class TActivity extends AutoLayoutActivity {

    protected void toastyInfo(String msg){
        Toasty.info(this,msg).show();
    }

}
