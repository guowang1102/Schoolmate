package com.weiguowang.schoolmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.config.AppConfig;
import com.weiguowang.schoolmate.entity.MyUser;
import com.weiguowang.schoolmate.event.NoticeEvent;
import com.weiguowang.schoolmate.view.CircleImageView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import cn.bmob.v3.BmobUser;

/**
 * function: 我的信息
 * Created by 韦国旺 on 2017/3/9.
 * Copyright (c) 2017  All Rights Reserved.
 */
public class MyInfoActivity extends TActivity implements View.OnClickListener {

    private TextView modifyTv;
    private Button logoutBtn;
    private CircleImageView headImg;
    private TextView nickNameTv, realNameTv, sexTv, jobTv, mobilePhoneTv, schoolNameTv, collegeTv, majorTv, sessionTv;

    public static final int CODE_BACK_MODIFY = 501;
    private int mHeight;
    private int mWidth;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_myinfo);
        initView();
        initEvent();
        initData();
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu)
    {
        getMenuInflater().inflate(R.menu.menu_modify, menu);
        return true;
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销事件接受
        EventBus.getDefault().unregister(this);
    }
    private void initView() {
        EventBus.getDefault().register(this);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.my_info));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        modifyTv = (TextView) findViewById(R.id.modify_tv);
        logoutBtn = (Button) findViewById(R.id.logout);
        headImg = (CircleImageView) findViewById(R.id.head_img);
        nickNameTv = (TextView) findViewById(R.id.nick_name);
        realNameTv = (TextView) findViewById(R.id.real_name);
        sexTv = (TextView) findViewById(R.id.sex);
        jobTv = (TextView) findViewById(R.id.job);
        mobilePhoneTv = (TextView) findViewById(R.id.mobile_phone);
        schoolNameTv = (TextView) findViewById(R.id.school_name);
        collegeTv = (TextView) findViewById(R.id.college);
        majorTv = (TextView) findViewById(R.id.major);
        sessionTv = (TextView) findViewById(R.id.session);
        headImg.post(new Runnable() {
            @Override
            public void run() {
                mWidth = headImg.getWidth();
                mHeight = headImg.getHeight();
            }
        });
    }

    private void initEvent() {
        logoutBtn.setOnClickListener(this);
//        modifyTv.setOnClickListener(this);
    }

    private void initData() {
        setUserInfo(BmobUser.getCurrentUser(MyUser.class));
        initHeadImg(headImg);

    }

    /**
     * @param myUser
     */
    private void setUserInfo(MyUser myUser) {
        nickNameTv.setText(myUser.getNickName());
        realNameTv.setText(myUser.getRealName());
        if(myUser.getSex()!=null){
            sexTv.setText(myUser.getSex() ? getString(R.string.female) : getString(R.string.male));
        }
        jobTv.setText(myUser.getJob());
        mobilePhoneTv.setText(myUser.getMobilePhoneNumber());
        schoolNameTv.setText(myUser.getSchoolName());
        collegeTv.setText(myUser.getCollege());
        majorTv.setText(myUser.getMajor());
        sessionTv.setText(myUser.getSession());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                finish();
                break;
            case R.id.action_modify:
                Intent intent = new Intent(MyInfoActivity.this, UpdateInfoActivity.class);
                startActivityForResult(intent, CODE_BACK_MODIFY);
                break;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CODE_BACK_MODIFY) {
            //TODO 刷新数据
            toastyInfo("修改用户数据返回");
            setUserInfo(BmobUser.getCurrentUser(MyUser.class));
        }
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()) {
            case R.id.logout: //退出登录
                BmobUser.logOut();   //清除缓存用户对象
                startActivity(new Intent(MyInfoActivity.this, SignInActivity.class));
                finish();
                break;
//            case R.id.modify_tv:  //修改
//                Intent intent = new Intent(MyInfoActivity.this, UpdateInfoActivity.class);
//                startActivityForResult(intent, CODE_BACK_MODIFY);
//                break;
        }
    }

    private static final String TAG = "myInfoActivity";

    @Subscribe
    public void onNoticeEvent(NoticeEvent event) {
        if (event.what == NoticeEvent.WHAT_UPDATE_HEAD) {
            //更新头像信息
            MyUser myUser = BmobUser.getCurrentUser(MyUser.class); //本地用户信息
            Log.d(TAG, "done: userInfo.getLastUpdateTime() " + myUser.getLastUpdateTime());
            Log.d(TAG, "done: myUser" + myUser.getHeadUrl());
            downloadHeadImg(new File(AppConfig.HEAD_IMG_LOCAL_PATH),headImg,myUser,true);
        }
    }


}
