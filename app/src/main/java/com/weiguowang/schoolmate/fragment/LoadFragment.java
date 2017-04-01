package com.weiguowang.schoolmate.fragment;

import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.view.ProgressWheel;

/**
 * function:
 * Created by 韦国旺 on 2017/4/1 0001.
 * Copyright (c) 2017 北京联龙博通 All Rights Reserved.
 */
public class LoadFragment extends DialogFragment {

    private ProgressWheel progressWheel;

    public static LoadFragment newInstance() {
        LoadFragment frag = new LoadFragment();
        return frag;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setCancelable(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View rootView = inflater.inflate(R.layout.layout_progress, null);
        progressWheel = (ProgressWheel) rootView.findViewById(R.id.progress_wheel);
        final int defaultBarColor = progressWheel.getBarColor();
        progressWheel.setBarColor(defaultBarColor);
        return rootView;
    }

}
