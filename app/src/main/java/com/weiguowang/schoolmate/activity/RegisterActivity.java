package com.weiguowang.schoolmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.entity.MyUser;
import com.weiguowang.schoolmate.utils.Toasty;

import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;

/**
 * function:
 * Created by 韦国旺 on 2017/3/8 0008.
 * Copyright (c) 2017  All Rights Reserved.
 */
public class RegisterActivity extends TActivity {

    private EditText emailEt, pwdEt;


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        initView();
    }

    private void initView() {
        emailEt = (EditText) findViewById(R.id.email);
        pwdEt = (EditText) findViewById(R.id.password);
    }


    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_btn:
                register();
                break;
            case R.id.signin_now:
                setResult("", "");
                break;
            default:
                break;
        }
    }

    private void setResult(String username, String password) {
        Intent intent = new Intent();
        Bundle b = new Bundle();
        b.putString("username", username);
        b.putString("password", password);
        intent.putExtras(b);
        setResult(RESULT_OK, intent);
        finish();
    }

    private void register() {
        MyUser myUser = new MyUser();
        myUser.setUsername(emailEt.getText().toString());
        myUser.setEmail(emailEt.getText().toString());
        myUser.setPassword(pwdEt.getText().toString());
        myUser.signUp(new SaveListener<MyUser>() {
            @Override
            public void done(MyUser myUser, BmobException e) {
                if (e == null) {
                    Toasty.success(RegisterActivity.this, "注册成功").show();
                    mHandler.sendEmptyMessageDelayed(WHAT_SUCCESS, 1000);
                } else {
                    Toasty.error(RegisterActivity.this, "注册失败:" + e.getMessage()).show();
                    mHandler.sendEmptyMessageDelayed(WHAT_ERROR, 1000);
                }
            }
        });
    }

    private static final int WHAT_SUCCESS = 0;
    private static final int WHAT_ERROR = 1;


    private Handler mHandler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case WHAT_SUCCESS:
                    setResult(emailEt.getText().toString(), pwdEt.getText().toString());
                    break;
                case WHAT_ERROR:
                    emailEt.setText("");
                    pwdEt.setText("");
                    break;

            }
        }
    };


}
