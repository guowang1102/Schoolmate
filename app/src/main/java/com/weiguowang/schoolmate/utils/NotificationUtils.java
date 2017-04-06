package com.weiguowang.schoolmate.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.weiguowang.schoolmate.R;

/**
 * function:
 * Created by 韦国旺 on 2017/4/5 0005.
 * Copyright (c) 2017 北京联龙博通 All Rights Reserved.
 */
public class NotificationUtils {

    @SuppressLint("NewApi")
    public static void showNotification(Context ctx, String... text) {
        NotificationManager notificationManager = (NotificationManager) ctx
                .getSystemService(Context.NOTIFICATION_SERVICE);
        Notification notification = new Notification(R.mipmap.ic_launcher, "通知",
                System.currentTimeMillis());
        notification.flags |= Notification.FLAG_ONGOING_EVENT; // ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
        notification.flags |= Notification.FLAG_AUTO_CANCEL; // �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�������������FLAG_ONGOING_EVENTһ��ʹ��
        notification.flags |= Notification.FLAG_SHOW_LIGHTS;
        notification.defaults = Notification.DEFAULT_LIGHTS | Notification.DEFAULT_SOUND | Notification.DEFAULT_VIBRATE;
        notification.ledARGB = Color.BLUE;
        notification.ledOnMS = 60000;
        CharSequence contentTitle = "";
        CharSequence contentText = "";
        if (text == null || text.length == 0) {
            contentText = "正文标题";
        } else {
            contentText = text[0];
        }
//        Intent notificationIntent = new Intent(ctx,
//                MyOrderActivity.class); // �����֪ͨ��Ҫ��ת��Activity
//        PendingIntent contentItent = PendingIntent.getActivity(ctx, 0,
//                notificationIntent, 0);
//        notification.setLatestEventInfo(ctx, contentTitle, contentText,
//                contentItent);
        notificationManager.notify(0, notification);
    }
}
