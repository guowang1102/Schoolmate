package com.weiguowang.schoolmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;

/**
 * function: 我的信息
 * Created by 韦国旺 on 2017/3/9.
 * Copyright (c) 2017  All Rights Reserved.
 */
public class MyInfoActivity extends TActivity implements View.OnClickListener {

    private TextView modifyTv;
    private Button logoutBtn;

    public static final int CODE_BACK_MODIFY = 501;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        initView();
        initEvent();

    }

    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle("我的信息");
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        modifyTv = (TextView) findViewById(R.id.modify_tv);
        logoutBtn = (Button) findViewById(R.id.logout);
    }

    private void initEvent() {
        logoutBtn.setOnClickListener(this);
        modifyTv.setOnClickListener(this);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CODE_BACK_MODIFY) {
            //TODO 刷新数据

        }
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.logout: //退出登录
                //
                break;
            case R.id.modify_tv:  //修改
                Intent intent = new Intent(MyInfoActivity.this, ModifyInfoActivity.class);
                startActivityForResult(intent, CODE_BACK_MODIFY);
                break;
        }
    }
}
