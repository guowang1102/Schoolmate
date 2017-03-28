package com.weiguowang.schoolmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.entity.MyUser;

import cn.bmob.v3.BmobUser;

/**
 * function:
 * Created by 韦国旺 on 2017/3/13 0013.
 * Copyright (c) 2017  All Rights Reserved.
 */
public class StartActivity extends TActivity {

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        initData();
    }

    private void initData(){
        MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
        if(userInfo!=null){
            startActivity(new Intent(this,MainActivity.class));
            finish();
        }else {
            startActivity(new Intent(this,SignInActivity.class));
            finish();
        }
    }
}
