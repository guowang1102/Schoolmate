package com.weiguowang.schoolmate;

import android.graphics.Bitmap;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.FragmentManager;
import android.text.TextUtils;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;

import com.weiguowang.schoolmate.adapter.SelectCallback;
import com.weiguowang.schoolmate.config.AppConfig;
import com.weiguowang.schoolmate.entity.MyUser;
import com.weiguowang.schoolmate.fragment.SelectFragment;
import com.weiguowang.schoolmate.utils.ImageUtils;
import com.weiguowang.schoolmate.utils.Toasty;
import com.zhy.autolayout.AutoLayoutActivity;

import java.io.File;
import java.util.List;

import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;

/**
 * function: activity基类
 * Created by on 2017/3/7 0007.
 * Copyright (c) 2017 All Rights Reserved.
 */
public abstract class TActivity extends AutoLayoutActivity {

    /**
     * toasty message
     *
     * @param msg
     */
    protected void toastyInfo(String msg) {
        Toasty.info(this, msg).show();
    }

    /**
     * 显示单行选择列表
     *
     * @param title
     * @param strList
     * @param tag
     * @param callback
     */
    protected void showSelectDialogFragment(String title, List<String> strList, String tag, SelectCallback callback) {
        FragmentManager fm = getSupportFragmentManager();
        SelectFragment dialogFragment = SelectFragment.newInstance(title, strList);
        dialogFragment.show(fm, tag);
        dialogFragment.setCallback(callback);
    }

    /**
     * @param resId
     * @param <T>
     * @return
     */
    protected <T extends View> T findView(int resId) {
        return (T) (findViewById(resId));
    }

    /**
     * 初始化用户头像
     *
     * @param imageView
     * @param width
     * @param height
     */
    protected void initHeadImg(final ImageView imageView, int width, int height) {
        new Handler() {
            @Override
            public void handleMessage(Message msg) {
                super.handleMessage(msg);
                final int mWidth = imageView.getWidth();
                final int mHeight = imageView.getHeight();
                //先去目录下寻找图片，如果找不到再下载
                File localFile = new File(AppConfig.HEAD_IMG_LOCAL_PATH);
                if (localFile.exists()) {
                    Log.d("info", "localFile is exists");
                    Log.d("info", "mWidth:" + mWidth);
                    Log.d("info", "mHeight:" + mHeight);
                    Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(localFile.getAbsolutePath(), mWidth, mHeight);
                    imageView.setImageBitmap(bitmap);
                } else {

                    Log.d("info", "localFile not ");
                    MyUser userInfo = BmobUser.getCurrentUser(MyUser.class);
                    if (!TextUtils.isEmpty(userInfo.getHeadUrl())) {
                        BmobFile bmobfile = new BmobFile(AppConfig.HEAD_IMG_NAME, "", userInfo.getHeadUrl());
                        final File saveFile = new File(AppConfig.HEAD_IMG_LOCAL_PATH);
                        if (!saveFile.getParentFile().exists()) {
                            saveFile.getParentFile().mkdir();
                        }
                        bmobfile.download(saveFile, new DownloadFileListener() {
                            @Override
                            public void done(String s, BmobException e) {
                                if (e == null) {
                                    Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(saveFile.getAbsolutePath(), mWidth, mHeight);
                                    imageView.setImageBitmap(bitmap);
                                }
                            }

                            @Override
                            public void onProgress(Integer integer, long l) {

                            }
                        });
                    }
                }

            }
        }.sendMessageDelayed(new Message(), 3000);
    }

}
