package com.weiguowang.schoolmate.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.widget.Toolbar;
import android.view.MenuItem;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.entity.MyUser;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.UpdateListener;

/**
 * function: 修改信息
 * Created by 韦国旺 on 2017/3/9.
 * Copyright (c) 2017  All Rights Reserved.
 */
public class ModifyInfoActivity extends TActivity implements View.OnClickListener {

    private EditText nickNameEt, realNameEt, sexEt, jobEt, mobilePhoneEt, schoolNameEt, collegeEt, majorEt, sessionEt;

    private TextView saveTv;

    private MyUser userInfo;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_modify_myinfo);
        initView();
        initData();
        initEvent();
    }



    private void initView() {
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        toolbar.setTitle(getString(R.string.modify_info));
        setSupportActionBar(toolbar);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        saveTv = (TextView) findViewById(R.id.save_tv);

        nickNameEt = (EditText) findViewById(R.id.nick_name);
        realNameEt = (EditText) findViewById(R.id.real_name);
        sexEt = (EditText) findViewById(R.id.sex);
        jobEt = (EditText) findViewById(R.id.job);
        mobilePhoneEt = (EditText) findViewById(R.id.mobile_phone);
        schoolNameEt = (EditText) findViewById(R.id.school_name);
        collegeEt = (EditText) findViewById(R.id.college);
        majorEt = (EditText) findViewById(R.id.major);
        sessionEt = (EditText) findViewById(R.id.session);
    }

    private void initData() {
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        setUserInfo(userInfo);
    }

    private void initEvent(){
        saveTv.setOnClickListener(this);
    }

    /**
     * @param myUser
     */
    private void setUserInfo(MyUser myUser) {
        nickNameEt.setText(myUser.getNickName());
        realNameEt.setText(myUser.getRealName());
        sexEt.setText(myUser.getSex() ? getString(R.string.female) : getString(R.string.male));
        jobEt.setText(myUser.getJob());
        mobilePhoneEt.setText(myUser.getMobilePhoneNumber());
        schoolNameEt.setText(myUser.getSchoolName());
        collegeEt.setText(myUser.getCollege());
        majorEt.setText(myUser.getMajor());
        sessionEt.setText(myUser.getSession());
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                setResult(RESULT_OK);
                finish();
                break;
        }
        return super.onOptionsItemSelected(item);
    }


    private void updateUserInfo() {
        MyUser myUser = new MyUser();
        myUser.setMobilePhoneNumber(mobilePhoneEt.getText().toString());
        myUser.setNickName(nickNameEt.getText().toString());
        myUser.setRealName(realNameEt.getText().toString());
        myUser.setSex(false);  //TODO
        myUser.setJob(jobEt.getText().toString());
        myUser.setSchoolName(schoolNameEt.getText().toString());
        myUser.setCollege(collegeEt.getText().toString());
        myUser.setMajor(majorEt.getText().toString());
        myUser.setSession(sessionEt.getText().toString());
        myUser.update(userInfo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toastyInfo("更新用户信息成功");
                    setResult(RESULT_OK);
                    finish();
                } else {
                    toastyInfo("更新用户信息失败:" + e.getMessage());
                }
            }
        });
    }


    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.save_tv:
                updateUserInfo();
                break;
        }

    }
}
