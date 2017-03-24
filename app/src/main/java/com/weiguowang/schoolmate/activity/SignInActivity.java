package com.weiguowang.schoolmate.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.entity.MyUser;
import com.weiguowang.schoolmate.utils.StatusBarCompat;
import com.weiguowang.schoolmate.utils.Toasty;
import com.weiguowang.schoolmate.view.ClearableEditText;
import com.zhy.autolayout.AutoRelativeLayout;

import java.lang.reflect.Field;

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

    private ClearableEditText userEt, pwdEt;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_signin);
        initView();
        testData();
    }


    private void testData() {
        userEt.setText("user");
        pwdEt.setText("1234");
        userEt.setSelection(userEt.getText().toString().length());
    }

    private void initView() {
        userEt = (ClearableEditText) findViewById(R.id.username);
        pwdEt = (ClearableEditText) findViewById(R.id.password);
//        Drawable userIcon = getResources().getDrawable(R.mipmap.ic_launcher);
//        Drawable userIcon = ContextCompat.getDrawable(this,R.mipmap.ic_launcher);
//        userIcon.setBounds(0,0,100,100);
//        userEt.setCompoundDrawables(userIcon,null,null,null);
//        userEt.setCompoundDrawablePadding(40);
////        userEt.setPadding(40,0,0,0);
//        userEt.setCompoundDrawablePadding(40);

//        Drawable pwdIcon = ContextCompat.getDrawable(this,R.mipmap.ic_launcher);
//        pwdIcon.setBounds(0,0,100,100);
//        pwdEt.setCompoundDrawables(pwdIcon,null,null,null);
//        if (Build.VERSION.SDK_INT >= 21) {
//            View decorView = getWindow().getDecorView();
//            int option = View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
//                    | View.SYSTEM_UI_FLAG_LAYOUT_STABLE;
//            decorView.setSystemUiVisibility(option);
//            getWindow().setStatusBarColor(Color.TRANSPARENT);
//        }
//        setColor(this,R.color.colorPrimary);
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
