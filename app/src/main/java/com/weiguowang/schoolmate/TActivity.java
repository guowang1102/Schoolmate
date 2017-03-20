package com.weiguowang.schoolmate;

import android.support.v4.app.FragmentManager;
import android.view.View;

import com.weiguowang.schoolmate.adapter.SelectCallback;
import com.weiguowang.schoolmate.fragment.SelectFragment;
import com.weiguowang.schoolmate.utils.Toasty;
import com.zhy.autolayout.AutoLayoutActivity;

import java.util.List;

/**
 * function:
 * Created by 韦国旺 on 2017/3/7 0007.
 * Copyright (c) 2017  All Rights Reserved.
 */
public abstract class TActivity extends AutoLayoutActivity {

    /**
     * toasty message
     *
     * @param msg
     */
    protected void toastyInfo(String msg) {
        Toasty.info(this, msg).show();
    }

    /**
     * 显示单行选择列表
     *
     * @param title
     * @param strList
     * @param tag
     * @param callback
     */
    protected void showSelectDialogFragment(String title, List<String> strList, String tag, SelectCallback callback) {
        FragmentManager fm = getSupportFragmentManager();
        SelectFragment dialogFragment = SelectFragment.newInstance(title, strList);
        dialogFragment.show(fm, tag);
        dialogFragment.setCallback(callback);
    }

    /**
     *
     * @param resId
     * @param <T>
     * @return
     */
    protected <T extends View> T findView(int resId) {
        return (T) (findViewById(resId));
    }

}
