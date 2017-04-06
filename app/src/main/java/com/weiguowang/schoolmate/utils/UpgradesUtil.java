package com.weiguowang.schoolmate.utils;

import android.app.ActivityManager;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager.NameNotFoundException;
import android.net.Uri;
import android.os.Environment;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

import java.io.File;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.List;

/**
 * function:
 * Created by 韦国旺 on 2017/4/5 0005.
 * Copyright (c) 2017 北京联龙博通 All Rights Reserved.
 */
public class UpgradesUtil {
    private Context mContext;
    // 保存路径
    private String mSavePath;
    // 保存文件名
    private String mName;
    // 下载地址
    private String mUrl;

    // 是否取消更新
    private boolean cancelUpdate = false;
    // 下载状态
    private static final int DOWNLOAD_STATUS = 1;
    // 下载结束
    private static final int DOWNLOAD_FINISH = 2;

    // 通知管理器
    private NotificationManager manager;
    private Notification notification;
    private int Progress = 0;        // 当前进度
    private int Max = 100;            // 最大进度

    public UpgradesUtil(Context mContext) {
        this.mContext = mContext;
    }

    /**
     * 设置下载条件
     *
     * @param mSavePath
     * @param mName
     * @param mUrl
     */
    public void setCondition(String mSavePath, String mName, String mUrl) {
        this.mSavePath = mSavePath;
        this.mName = mName;
        this.mUrl = mUrl;
    }

    Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            // TODO Auto-generated method stub
            switch (msg.what) {
                case DOWNLOAD_STATUS:
                    // 设置显示下载进度，刷新Notification
//                    notification.contentView.setTextViewText(R.id.content_view_tv, "下载进度：" + Progress + "%");
//                    notification.contentView.setProgressBar(R.id.content_view_progress, Max, Progress, false);
//                    manager.notify(DOWNLOAD_STATUS, notification);
                    if (Progress >= Max) {
                        Log.v("DOWNLOAD_STATUS", "下载进度100%");
                        manager.cancel(DOWNLOAD_STATUS);
                    }
                    break;
                case DOWNLOAD_FINISH:
                    // 安装APK
//                InstallApk();
                    Log.v("DOWNLOAD_FINISH", "安装调用");
                    manager.cancel(DOWNLOAD_STATUS);
                    break;

                default:
                    break;
            }
            super.handleMessage(msg);
        }
    };

    /**
     * 获取当前版本
     *
     * @return
     */
    public int getVersion() {
        try {
            return mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
        } catch (NameNotFoundException e) {
            // TODO Auto-generated catch block
            Log.v("获取当前版本", e.toString());
            return -1;
        }
    }

    /**
     * 检查是否需要更新
     *
     * @param NewVersion
     * @return
     */
    public boolean UpgradesMethod(int NewVersion) {
        try {
            int version = mContext.getPackageManager().getPackageInfo(mContext.getPackageName(), 0).versionCode;
            if (version < NewVersion) {
                return true;
            } else return false;
        } catch (Exception e) {
            // TODO Auto-generated catch block
            Log.v("检查是否需要更新", e.toString());
            return false;
        }
    }

    /**
     * 下载APK
     */
    public void DownloadApk() {
        NotificationShow();
        // 启动线程下载
        new Thread(LoadData).start();
    }

    Runnable LoadData = new Runnable() {

        @Override
        public void run() {
            // TODO Auto-generated method stub
            try {
                // 判断SD卡是否存在，并且是否具有读写权限
                if (Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED)) {

                    // 创建连接
                    URL url = new URL(mUrl);
                    HttpURLConnection conn = (HttpURLConnection) url.openConnection();
                    conn.connect();
                    // 获取文件大小
                    int length = conn.getContentLength();
                    // 创建输入流
                    InputStream is = conn.getInputStream();
                    File file = new File(mSavePath);
                    // 判断文件目录是否存在
                    if (!file.exists()) {
                        file.mkdir();
                    }
                    File apkFile = new File(mSavePath, mName);
                    FileOutputStream fos = new FileOutputStream(apkFile);
                    int count = 0;
                    // 缓存
                    byte buf[] = new byte[1024];
                    // 写入到文件中
                    do {
                        int numread = is.read(buf);
                        count += numread;
                        // 计算进度条位置
                        Progress = (int) (((float) count / length) * 100);
                        //  更新进度
                        mHandler.sendEmptyMessage(DOWNLOAD_STATUS);
                        if (numread <= 0) {
                            // 下载完成
                            Log.v("下载", "完成" + numread);
                            mHandler.sendEmptyMessage(DOWNLOAD_FINISH);
                            break;
                        }
                        // 写入文件
                        fos.write(buf, 0, numread);
                    } while (!cancelUpdate); // 点击取消就停止下载.
                    fos.close();
                    is.close();
                }
            } catch (Exception e) {
                // TODO Auto-generated catch block
                Log.v("APK下载", e.toString());
            }
        }
    };

    /**
     * 显示通知
     */
    public void NotificationShow() {
        // 1.得到通知管理器
        manager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);

        // 2.构建通知
        notification = new Notification();
//        notification.icon = R.drawable.my_app_logo;
        notification.tickerText = "更新检查";

        // 3.设置通知点击事件
        Intent intent = new Intent(mContext, mContext.getClass());
        PendingIntent contentIntent = PendingIntent.getActivity(mContext, 100, intent, 0);
        notification.contentIntent = contentIntent;
        notification.flags |= Notification.FLAG_ONGOING_EVENT;    // 放置在"正在运行"栏目中
        notification.flags |= Notification.FLAG_AUTO_CANCEL;    // 该通知能被状态栏的清除按钮给清除掉
        notification.defaults = Notification.DEFAULT_SOUND;        // 声音默认
        notification.defaults = Notification.DEFAULT_LIGHTS;    // 使用默认闪光提示
//        notification.defaults = Notification.DEFAULT_VIBRATE;	// 设定震动(需加VIBRATE权限)

        // 4.设置通知显示的布局
//        notification.contentView = new RemoteViews(mContext.getPackageName(), R.layout.content_view);

        // 5.发送通知
        manager.notify(DOWNLOAD_STATUS, notification);

        // 6.下载APK
//        DownloadApk();
    }

    /**
     * 安装APK
     */
    private void InstallApk() {
        File file = new File(mSavePath, mName);
        if (!file.exists()) {
            return;
        }
        // 通过Intent安装APK
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setDataAndType(Uri.parse("file://" + file.toString()), "application/vnd.android.package-archive");
        mContext.startActivity(intent);
    }

    /**
     * 获取服务是否开启
     *
     * @param context   上下文
     * @param className 完整包名的服务类名
     * @return true: 是<br>false: 否
     */
    public static boolean isRunningService(Context context, String className) {
        // 进程的管理者,活动的管理者
        ActivityManager activityManager = (ActivityManager)
                context.getSystemService(Context.ACTIVITY_SERVICE);
        // 获取正在运行的服务，最多获取1000个
        List<ActivityManager.RunningServiceInfo> runningServices = activityManager.getRunningServices(1000);
        // 遍历集合
        for (ActivityManager.RunningServiceInfo runningServiceInfo : runningServices) {
            ComponentName service = runningServiceInfo.service;
            if (className.equals(service.getClassName())) {
                return true;
            }
        }
        return false;
    }
}
