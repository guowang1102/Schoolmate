package com.weiguowang.schoolmate.activity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.entity.MyUser;
import com.weiguowang.schoolmate.entity.School;
import com.weiguowang.schoolmate.view.CircleImageView;
import com.weiguowang.schoolmate.view.CircleMenuLayout;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.SaveListener;
import cn.bmob.v3.listener.UpdateListener;

public class MainActivity extends TActivity {

    private CircleMenuLayout mCircleMenuLayout;
    private String[] mItemTexts = new String[]{"安全中心 ", "特色服务", "投资理财", "转账汇款", "我的账户", "信用卡", "安全中心 ", "特色服务", "投资理财",
            "转账汇款", "我的账户", "信用卡", "15KM ", "特色服务", "投资理财"};
    private int[] mItemImgs = new int[]{R.mipmap.ic_launcher, R.mipmap.menu1,
            R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1,
            R.mipmap.menu1, R.mipmap.menu1, R.mipmap.ic_launcher, R.mipmap.menu1, R.mipmap.ic_launcher, R.mipmap.menu1, R.mipmap.menu1, R.mipmap.menu1};

    private CircleImageView myInfoImg;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setEvent();
        initData();
//        testData();

//        School school = new School();
//        school.setSchoolName("广西建设职业技术学院");
//        school.setMajor("计算机网络技术");
//        school.setCollege("计算机系");
//        school.setSession("网络1102");
//        school.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//
//            }
//        });

//        school.clear();
//        school.setSchoolName("广西财经学院");
//        school.setMajor("资产评估");
//        school.setCollege("工商管理学院");
//        school.setSession("资评1101");
//        school.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//
//            }
//        });
//
//        school.clear();
//        school.setSchoolName("广西财经学院");
//        school.setMajor("资产评估");
//        school.setCollege("工商管理学院");
//        school.setSession("资评1101");
//        school.save(new SaveListener<String>() {
//            @Override
//            public void done(String s, BmobException e) {
//
//            }
//        });


    }

    private MyUser userInfo;

    private void initData() {
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        if ("".equals(userInfo.getSchoolName())) {
            //TODO 识别个人信息是否完整，不完整的就弹出
            toastyInfo("个人信息不完整");
        }
    }

    private void testData() {
        MyUser myUser = new MyUser();
        myUser.setNickName("爵爷");
        myUser.setRealName("韦国旺");
        myUser.setSex(false);
        myUser.setJob("Android工程师");
        myUser.setSchoolName("广西建设职业技术学院");
        myUser.setCollege("计算机系");
        myUser.setMajor("计算机网络技术");
        myUser.setSession("网络1102");
        myUser.update(userInfo.getObjectId(), new UpdateListener() {
            @Override
            public void done(BmobException e) {
                if (e == null) {
                    toastyInfo("更新用户信息成功");
                } else {
                    toastyInfo("更新用户信息失败:" + e.getMessage());
                }
            }
        });
    }

    private void initView() {
        mCircleMenuLayout = (CircleMenuLayout) findViewById(R.id.id_menulayout);
        mCircleMenuLayout.setMenuItemIconsAndTexts(mItemImgs, mItemTexts);
        myInfoImg = (CircleImageView) findViewById(R.id.myinfo_img);
    }

    private void setEvent() {
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
        myInfoImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyInfoActivity.class));
            }
        });
    }

}
