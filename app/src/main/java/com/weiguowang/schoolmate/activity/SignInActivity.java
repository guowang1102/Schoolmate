package com.weiguowang.schoolmate.activity;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.View;
import android.view.WindowManager;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.bluelinelabs.logansquare.LoganSquare;
import com.tencent.connect.UserInfo;
import com.tencent.connect.auth.QQToken;
import com.tencent.connect.common.Constants;
import com.tencent.tauth.IUiListener;
import com.tencent.tauth.Tencent;
import com.tencent.tauth.UiError;
import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.entity.MyUser;
import com.weiguowang.schoolmate.entity.QQResult;
import com.weiguowang.schoolmate.utils.StatusBarCompat;
import com.weiguowang.schoolmate.utils.Toasty;
import com.weiguowang.schoolmate.view.ClearableEditText;
import com.zhy.autolayout.AutoRelativeLayout;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.lang.reflect.Field;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.LogInListener;
import cn.bmob.v3.listener.UpdateListener;

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
        mTencent = Tencent.createInstance(APP_ID, this.getApplicationContext());
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

//    private PrefsUtil prefsUtil = PrefsUtil.getInstance();

    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.register_now:
                Intent intent = new Intent(SignInActivity.this, RegisterActivity.class);
                startActivityForResult(intent, CODE_BACK_REGISTER);
                break;
            case R.id.signin_btn:   //登录
                signIn();
                break;
            case R.id.forget_pwd:   //忘记密码
                toastyInfo("功能未实现，请期待...");
                break;
            case R.id.qqlogin_btn:  //QQ 登录
                qqLogin();
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

    private static final String APP_ID = "1106065174";//官方获取的APPID
    private Tencent mTencent;
    private BaseUiListener mIUiListener;
    private UserInfo mUserInfo;

    private void qqLogin() {
        mIUiListener = new BaseUiListener();

//        String openid = prefsUtil.getPrefsStrValue(Config.KEY_QQ_OPENID);
//        String access_token = prefsUtil.getPrefsStrValue(Config.KEY_QQ_TOKEN);
//        String expires_in = prefsUtil.getPrefsStrValue(Config.KEY_QQ_EXPIRES);
//        if(!openid.isEmpty()&&!access_token.isEmpty()&&!expires_in.isEmpty()){
//            mTencent.setOpenId(openid);
//            mTencent.setAccessToken(access_token, expires_in);
//        }
        mTencent.login(SignInActivity.this, "all", mIUiListener);

    }

    QQResult qqResult;


    private class BaseUiListener implements IUiListener {

        @Override
        public void onComplete(Object response) {
            Toast.makeText(SignInActivity.this, "授权成功", Toast.LENGTH_SHORT).show();
            Log.e(TAG, "response:" + response);
            JSONObject obj = (JSONObject) response;
            try {
                final String openID = obj.getString("openid");
                final String accessToken = obj.getString("access_token");
                final String expires = obj.getString("expires_in");
                mTencent.setOpenId(openID);
                mTencent.setAccessToken(accessToken, expires);
                QQToken qqToken = mTencent.getQQToken();
                mUserInfo = new UserInfo(getApplicationContext(), qqToken);
                mUserInfo.getUserInfo(new IUiListener() {
                    @Override
                    public void onComplete(Object response) {
                        Log.e(TAG, "登录成功" + response.toString());

                        String jsonString = response.toString();
                        try {
                            qqResult = LoganSquare.parse(jsonString, QQResult.class);
                            Log.e(TAG, "LoganSquare解析正常" + qqResult.nickname);
                        } catch (IOException e) {
                            Log.e(TAG, "LoganSquare解析异常" + e.getMessage());
                            e.printStackTrace();
                        }


                        BmobUser.BmobThirdUserAuth authInfo = new BmobUser.BmobThirdUserAuth(BmobUser.BmobThirdUserAuth.SNS_TYPE_QQ, accessToken, expires, openID);
                        loginWithAuth(authInfo);
                    }


                    @Override
                    public void onError(UiError uiError) {
                        Log.e(TAG, "登录失败" + uiError.toString());
                    }

                    @Override
                    public void onCancel() {
                        Log.e(TAG, "登录取消");

                    }
                });
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

        @Override
        public void onError(UiError uiError) {
            Toast.makeText(SignInActivity.this, "授权失败", Toast.LENGTH_SHORT).show();

        }

        @Override
        public void onCancel() {
            Toast.makeText(SignInActivity.this, "授权取消", Toast.LENGTH_SHORT).show();

        }

    }

    private void loginWithAuth(BmobUser.BmobThirdUserAuth authInfo) {
        BmobUser.loginWithAuthData(authInfo, new LogInListener<JSONObject>() {
            @Override
            public void done(JSONObject jsonObject, BmobException e) {
                if(e==null){
                    MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
                    userInfo.setNickName(qqResult.nickname);
                    userInfo.update(new UpdateListener() {
                        @Override
                        public void done(BmobException e) {
                            //更新成功
                            Log.e(TAG, "更新成功");
                        }
                    });

                    Log.d(TAG,"loginWithAuth登陆成功返回:"+jsonObject);
                    Toasty.success(SignInActivity.this, "登录成功").show();
                    Intent intent = new Intent(SignInActivity.this, MainActivity.class);
                    startActivity(intent);
                    finish();
                }else {
                    Log.d(TAG,"loginWithAuth登陆失败:"+e.getMessage());
                    Toasty.error(SignInActivity.this,"QQ登录失败"+e.getMessage()).show();
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
        if (requestCode == Constants.REQUEST_LOGIN) {
            Tencent.onActivityResultData(requestCode, resultCode, data, mIUiListener);
        }


    }
}
