package com.weiguowang.schoolmate.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;

import com.weiguowang.schoolmate.config.AppConfig;
import com.weiguowang.schoolmate.event.MessageEvent;
import com.weiguowang.schoolmate.event.NoticeEvent;
import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.entity.MyUser;
import com.weiguowang.schoolmate.utils.ImageUtils;
import com.weiguowang.schoolmate.view.CircleImageView;
import com.weiguowang.schoolmate.view.CircleMenuLayout;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;

import java.io.File;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

public class MainActivity extends TActivity {

    private CircleMenuLayout mCircleMenuLayout;
    private String[] mItemTexts = new String[]{"安全中心 ", "特色服务", "投资理财", "转账汇款", "我的账户", "信用卡", "安全中心 ", "特色服务", "投资理财",
            "转账汇款", "我的账户", "信用卡", "15KM ", "特色服务", "投资理财"};
    private int[] mItemImgs = new int[]{R.mipmap.ic_launcher, R.mipmap.menu1,
            R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1,
            R.mipmap.menu1, R.mipmap.menu1, R.mipmap.ic_launcher, R.mipmap.menu1, R.mipmap.ic_launcher, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1};

    private CircleImageView headImg;
    private int mHeight;
    private int mWidth;
    private MyUser userInfo;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        initEvent();
        initData();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        //注销事件接受
        EventBus.getDefault().unregister(this);
    }


    private void initData() {
        EventBus.getDefault().register(this);
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        if ("".equals(userInfo.getSchoolName())) {
            //TODO 识别个人信息是否完整，不完整的就弹出
            toastyInfo("个人信息不完整");
        }
        initHeadImg(headImg);
    }

    private void initView() {
        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
        headImg = (CircleImageView) findViewById(R.id.myinfo_img);
        headImg.post(new Runnable() {
            @Override
            public void run() {
                mWidth = headImg.getWidth();
                mHeight = headImg.getHeight();
            }
        });
    }

    private void initEvent() {
        mCircleMenuLayout.setOnMenuItemClickListener(new CircleMenuLayout.OnMenuItemClickListener() {
            @Override
            public void itemClick(View view, int pos) {
                toastyInfo("Click " + mItemTexts[pos]);
            }

            @Override
            public void itemCenterClick(View view) {
                toastyInfo("Click Center");
            }
        });
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyInfoActivity.class));
            }
        });
    }

    @Subscribe
    public void onNoticeEvent(NoticeEvent event) {
        if (event.what == NoticeEvent.WHAT_UPDATE_HEAD) {
            //更新头像信息
            MyUser myUser = BmobUser.getCurrentUser(MyUser.class); //本地用户信息
            Log.d("MainActivity", "done: userInfo.getLastUpdateTime() " + myUser.getLastUpdateTime());
            downloadHeadImg(new File(AppConfig.HEAD_IMG_LOCAL_PATH), headImg, myUser, true);
        }
    }

}
