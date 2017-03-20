package com.weiguowang.schoolmate.activity;

import android.content.Intent;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.os.Environment;
import android.text.TextUtils;
import android.view.View;

import com.weiguowang.schoolmate.R;
import com.weiguowang.schoolmate.TActivity;
import com.weiguowang.schoolmate.entity.MyUser;
import com.weiguowang.schoolmate.utils.ImageUtils;
import com.weiguowang.schoolmate.view.CircleImageView;
import com.weiguowang.schoolmate.view.CircleMenuLayout;

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

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        setEvent();
        initData();
    }

    private MyUser userInfo;

    private void initData() {
        userInfo = BmobUser.getCurrentUser(MyUser.class);
        if ("".equals(userInfo.getSchoolName())) {
            //TODO 识别个人信息是否完整，不完整的就弹出
            toastyInfo("个人信息不完整");
        }

        if (!TextUtils.isEmpty(userInfo.getHeadUrl())) {
            toastyInfo("userInfo is not null");
            BmobFile bmobfile =new BmobFile("abc.png","",userInfo.getHeadUrl());
            final File saveFile = new File(Environment.getExternalStorageDirectory(), bmobfile.getFilename());
            bmobfile.download(saveFile, new DownloadFileListener() {
                @Override
                public void done(String s, BmobException e) {
                    if(e==null){

                        Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(saveFile.getAbsolutePath(), mWidth, mHeight);
                        headImg.setImageBitmap(bitmap);

//                        toast("下载成功,保存路径:"+savePath);
                    }else{
//                        toast("下载失败："+e.getErrorCode()+","+e.getMessage());
                    }
                }

                @Override
                public void onProgress(Integer integer, long l) {

                }
            });
        }else {
            toastyInfo("userInfo is null");
        }
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
        headImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(MainActivity.this, MyInfoActivity.class));
            }
        });
    }

}
