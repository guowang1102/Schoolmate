package com.weiguowang.schoolmate.utils;

import android.annotation.SuppressLint;
import android.app.Notification;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;

import com.anmoke.R;
import com.anmoke.activity.me.MyOrderActivity;

/**
 * notificaion������
 * @author Administrator
 *
 */
public class NotificationUtils {

	@SuppressLint("NewApi")
	public static void showNotification(Context ctx,String... text) {
		// ����һ��NotificationManager������
		NotificationManager notificationManager = (NotificationManager) ctx
				.getSystemService(Context.NOTIFICATION_SERVICE);
		// ����Notification�ĸ�������
		Notification notification = new Notification(R.drawable.notificaion_icon, "����֪ͨ",
				System.currentTimeMillis());
		// FLAG_AUTO_CANCEL ��֪ͨ�ܱ�״̬���������ť�������
		// FLAG_NO_CLEAR ��֪ͨ���ܱ�״̬���������ť�������
		// FLAG_ONGOING_EVENT ֪ͨ��������������
		// FLAG_INSISTENT �Ƿ�һֱ���У���������һֱ���ţ�֪���û���Ӧ
		notification.flags |= Notification.FLAG_ONGOING_EVENT; // ����֪ͨ�ŵ�֪ͨ����"Ongoing"��"��������"����
		notification.flags |= Notification.FLAG_AUTO_CANCEL; // �����ڵ����֪ͨ���е�"���֪ͨ"�󣬴�֪ͨ�������������FLAG_ONGOING_EVENTһ��ʹ��
		notification.flags |= Notification.FLAG_SHOW_LIGHTS;
		// DEFAULT_ALL ʹ������Ĭ��ֵ�������������𶯣������ȵ�
		// DEFAULT_LIGHTS ʹ��Ĭ��������ʾ
		// DEFAULT_SOUNDS ʹ��Ĭ����ʾ����
		// DEFAULT_VIBRATE ʹ��Ĭ���ֻ��𶯣������<uses-permission
		// android:name="android.permission.VIBRATE" />Ȩ��
		// ����Ч������
		notification.defaults=Notification.DEFAULT_LIGHTS|Notification.DEFAULT_SOUND|Notification.DEFAULT_VIBRATE;
		notification.ledARGB = Color.BLUE;
		notification.ledOnMS = 60000; // ����ʱ�䣬����
		// ����֪ͨ���¼���Ϣ
		CharSequence contentTitle = "����֪ͨ"; // ֪ͨ������
		CharSequence contentText = "";
		if(text == null || text.length == 0){
			contentText = "���ж���֪ͨ����鿴";
		}else{
			contentText = text[0];
		}
		Intent notificationIntent = new Intent(ctx,
				MyOrderActivity.class); // �����֪ͨ��Ҫ��ת��Activity
		PendingIntent contentItent = PendingIntent.getActivity(ctx, 0,
				notificationIntent, 0);
		notification.setLatestEventInfo(ctx, contentTitle, contentText,
				contentItent);
		// ��Notification���ݸ�NotificationManager
		notificationManager.notify(0, notification);
	}

}
