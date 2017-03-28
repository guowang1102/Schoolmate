package com.weiguowang.schoolmate.utils;

import java.io.File;
import java.io.FileOutputStream;

import org.xmlpull.v1.XmlSerializer;

import android.app.ProgressDialog;
import android.content.ContentResolver;
import android.content.Context;
import android.database.Cursor;
import android.net.Uri;
import android.util.Xml;
import android.widget.ProgressBar;

public class SmsBackupUtils {
	
	
	//СB����Ա��Ƶ���ĸĶ�ģ�������ǽӿ�
	
	public interface SmsBackupCallBack{
		/**
		 * ���ű���ǰ����
		 * @param total ��������
		 */
		public void smsBackupBefore(int total);
		/**
		 * ���ű��ݹ����е���
		 * @param progress ���ű��ݽ���
		 */
		public void smsBackupProgress(int progress);
		
		
	}
	
	/**
	 * ���ű���
	 * @param context ������
	 * @param path ����·��
	 * @throws Exception 
	 */
	
	public static void smsBackup(Context context,String path,SmsBackupCallBack callBack) throws Exception{
		
		//Xml�����л���
		XmlSerializer serializer = Xml.newSerializer();
		File file = new File(path);
		FileOutputStream os = new FileOutputStream(file);
		//���ò���
		serializer.setOutput(os, "utf-8");//���ñ��룬�ļ���
		serializer.startDocument("utf-8", true);
		serializer.startTag(null, "smss");
		
		ContentResolver resolver = context.getContentResolver();
		Uri uri = Uri.parse("content://sms");
		Cursor cursor = resolver.query(uri, new String[]{"address","date","type","body"}, null, null, null);
//		dialog.setMax(cursor.getCount());//����������
//		progressBar1.setMax(cursor.getCount());
		callBack.smsBackupBefore(cursor.getCount());
		int progress = 0;
		while(cursor.moveToNext()){
			serializer.startTag(null, "sms");
			
			
			serializer.startTag(null, "address");
			String address = cursor.getString(0);
			serializer.text(address);
			serializer.endTag(null, "address");
			
			serializer.startTag(null, "date");
			String date = cursor.getString(1);
			serializer.text(date);
			serializer.endTag(null, "date");
			
			serializer.startTag(null, "type");
			String type = cursor.getString(2);
			serializer.text(type);
			serializer.endTag(null, "type");
			
			serializer.startTag(null, "body");
			String body = cursor.getString(3);
			serializer.text(body);
			serializer.endTag(null, "body");
			
			serializer.endTag(null, "sms");
			
			progress ++;
//			dialog.setProgress(progress);
//			progressBar1.setProgress(progress);
			callBack.smsBackupProgress(progress);
			Thread.sleep(1000);
		}
		cursor.close();
		serializer.endTag(null, "smss");
		serializer.endDocument();
		
		
	}

}
