package com.weiguowang.schoolmate.fragment;

import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.DialogFragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.ImageView;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.adapter.SelectAdapter;
import com.weiguowang.schoolmate.adapter.SelectCallback;
import com.weiguowang.schoolmate.view.recycler.HorizontalDividerItemDecoration;

import java.util.ArrayList;
import java.util.List;

/**
 * function:列表选项
 * Created by 韦国旺 on 2016/11/2.
 * Copyright (c) 2016  All Rights Reserved.
 */

public class SelectFragment extends DialogFragment {

    private static final String ARGS_KEY_TITLE = "title";
    private static final String ARGS_KEY_STRLIST = "strList";

    private RecyclerView mRecyclerView;
    private SelectAdapter adapter;
    private LinearLayoutManager linearLayoutManager;

    public SelectFragment() {

    }

    @Override
    public void onStart() {
        super.onStart();
        Dialog dialog = getDialog();
        if (dialog != null) {
            DisplayMetrics dm = new DisplayMetrics();
            getActivity().getWindowManager().getDefaultDisplay().getMetrics(dm);
            dialog.getWindow().setLayout((int) (dm.widthPixels * 0.7), ViewGroup.LayoutParams.WRAP_CONTENT);
        }
    }

    public static SelectFragment newInstance(String title, List<String> strList) {
        SelectFragment frag = new SelectFragment();
        Bundle args = new Bundle();
        args.putString(ARGS_KEY_TITLE, title);
        args.putStringArrayList(ARGS_KEY_STRLIST, (ArrayList<String>) strList);
        frag.setArguments(args);
        return frag;
    }

    private SelectCallback callback;

    public void setCallback(SelectCallback callback) {
        this.callback = callback;
    }


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        //如果setCancelable()中参数为true，若点击dialog覆盖不到的activity的空白或者按返回键，
        //则进行cancel，状态检测依次onCancel()和onDismiss()。如参数为false，则按空白处或返回键无反应。缺省为true
        super.onCreate(savedInstanceState);
        setCancelable(true);
        //可以设置dialog的显示风格
        //setStyle(style,theme);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        Log.d("llbt", "Tag is" + getTag()); // tag which is from acitivity which started this fragment
        getDialog().requestWindowFeature(Window.FEATURE_NO_TITLE);
        getDialog().getWindow().setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        View rootView = inflater.inflate(R.layout.fragment_select, null);
        mRecyclerView = (RecyclerView) rootView.findViewById(R.id.recyclerview);
        mRecyclerView.addItemDecoration(new HorizontalDividerItemDecoration.Builder(getActivity())
                .color(R.color.select_divider)
                .size(getResources().getDimensionPixelSize(R.dimen.divider))
                .margin(getResources().getDimensionPixelSize(R.dimen.topmargin),
                        getResources().getDimensionPixelSize(R.dimen.bottommargin)).build());
        // 设置布局管理器
        linearLayoutManager = new LinearLayoutManager(getActivity());
        linearLayoutManager.setOrientation(LinearLayoutManager.VERTICAL);
        mRecyclerView.setLayoutManager(linearLayoutManager);


        final List<String> strList = getArguments().getStringArrayList(ARGS_KEY_STRLIST);
        adapter = new SelectAdapter(getActivity(), strList);
        mRecyclerView.setAdapter(adapter);
        adapter.setOnItemClickListener(new SelectAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                if (callback != null) {
                    callback.getValue(strList.get(position), position);
                }
                dismiss();
            }
        });

        ImageView closeBtn = (ImageView) rootView.findViewById(R.id.select_close_btn);
        closeBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return rootView;
    }


}
