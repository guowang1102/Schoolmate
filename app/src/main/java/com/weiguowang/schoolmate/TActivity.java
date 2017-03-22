package com.weiguowang.schoolmate;

import android.graphics.Bitmap;
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

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.BmobUser;
import cn.bmob.v3.datatype.BmobFile;
import cn.bmob.v3.exception.BmobException;
import cn.bmob.v3.listener.DownloadFileListener;
import cn.bmob.v3.listener.FindListener;

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
     * <p>
     * 如果是刚启动应用，查询最后更新时间是否和本地的相等，如果相等，去查找本地目录下是否有图片，如果有直接显示，如果没有就下载
     * 不是刚启动应用，去查找本地目录下是否有图片，如果有直接显示，如果没有就下载
     *
     * @param imageView
     */
    protected void initHeadImg(final ImageView imageView, boolean isFirst) {
        final File localFile = new File(AppConfig.HEAD_IMG_LOCAL_PATH);
        final MyUser userInfo = BmobUser.getCurrentUser(MyUser.class); //本地用户信息
        if (isFirst) {
            final long lastHeadUpdateTime = userInfo.getLastUpdateTime();
            BmobQuery<MyUser> query = new BmobQuery<>();
            query.addWhereEqualTo("username", userInfo.getUsername());
            query.findObjects(new FindListener<MyUser>() {
                @Override
                public void done(List<MyUser> object, BmobException e) {
                    if (e == null) {
                        MyUser myUser = object.get(0);
                        if (myUser.getLastUpdateTime() != lastHeadUpdateTime) {
                            if (!TextUtils.isEmpty(myUser.getHeadUrl())) {
                                BmobFile bmobfile = new BmobFile(AppConfig.HEAD_IMG_NAME, "", myUser.getHeadUrl());
                                if (!localFile.getParentFile().exists()) {
                                    localFile.getParentFile().mkdir();
                                }
                                bmobfile.download(localFile, new DownloadFileListener() {
                                    @Override
                                    public void done(String s, BmobException e) {
                                        if (e == null) {
                                            Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(localFile.getAbsolutePath(), 0, 0);
                                            imageView.setImageBitmap(bitmap);
                                            userInfo.update(); //更新本地用户数据
                                            Log.d("TActivity", "done: userinfo lastupdatetime is " + userInfo.getLastUpdateTime());
                                        }
                                    }

                                    @Override
                                    public void onProgress(Integer integer, long l) {

                                    }
                                });
                            }
                        } else {
                            downloadHeadImg(localFile, imageView, userInfo, false);
                        }
                    } else {
                        toastyInfo("用户头像更新失败");
                    }
                }
            });
        } else { //不是启动后第一次加载用户头像
            downloadHeadImg(localFile, imageView, userInfo, false);
        }
    }

    /**
     * 下载用户头像 （如果本地路径有了就直接加载，如果没有就下载）
     *
     * @param localFile
     * @param imageView
     * @param myUser
     */
    protected void downloadHeadImg(final File localFile, final ImageView imageView, MyUser myUser, boolean isForce) {
        if (isForce || !localFile.exists()) {
            Log.d("info","进到这里");
            if (!TextUtils.isEmpty(myUser.getHeadUrl())) {
                BmobFile bmobfile = new BmobFile(AppConfig.HEAD_IMG_NAME, "", myUser.getHeadUrl());
                if (!localFile.getParentFile().exists()) {
                    localFile.getParentFile().mkdir();
                }
                bmobfile.download(localFile, new DownloadFileListener() {
                    @Override
                    public void done(String s, BmobException e) {
                        if (e == null) {
                            Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(localFile.getAbsolutePath(), 0, 0);
                            imageView.setImageBitmap(bitmap);
                        }
                    }

                    @Override
                    public void onProgress(Integer integer, long l) {

                    }
                });
            }
        } else if (localFile.exists()) {
            Log.d("info","进到这里localFile");
            Bitmap bitmap = ImageUtils.decodeSampledBitmapFromFile(localFile.getAbsolutePath(), 0, 0);
            imageView.setImageBitmap(bitmap);
        }
    }


    /**
     * 非第一次初始化头像
     *
     * @param imageView
     */
    protected void initHeadImg(final ImageView imageView) {
        initHeadImg(imageView, false);
    }

}
