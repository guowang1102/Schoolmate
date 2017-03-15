package com.weiguowang.schoolmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.View;
import android.widget.EditText;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.entity.MyUser;
import com.weiguowang.schoolmate.utils.Toasty;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;

/**
 * function:
 * Created by 韦国旺 on 2017/3/8 0008.
 * Copyright (c) 2017  All Rights Reserved.
 */
public class SignInActivity extends TActivity {

    public static final int CODE_BACK_REGISTER = 0;

    private EditText userEt, pwdEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initView();
        testData();
    }

    private void testData(){
        userEt.setText("user");
        pwdEt.setText("1234");
    }

    private void initView() {
        userEt = (EditText) findViewById(R.id.username);
        pwdEt = (EditText) findViewById(R.id.password);
    }

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_now:
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivityForResult(intent, CODE_BACK_REGISTER);
                break;
            case R.id.signin_btn:
                signIn();
                break;
        }
    }

    private void signIn() {
        String username = userEt.getText().toString().trim();
        String password = pwdEt.getText().toString().trim();
        BmobUser.loginByAccount(username, password, new LogInListener<MyUser>() {

            @Override
            public void done(MyUser user, BmobException e) {
                if (user != null) {
                    Toasty.success(SignInActivity.this, "登录成功").show();
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                } else {
                    Toasty.error(SignInActivity.this, "登录失败:" + e.getMessage()).show();
                }
            }
        });

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        if (resultCode == RESULT_OK && requestCode == CODE_BACK_REGISTER) {
            Bundle b = data.getExtras();
            String username = b.getString("username");
            String password = b.getString("password");
            userEt.setText(b.getString("username"));
            pwdEt.setText(b.getString("password"));

        }
    }
}
