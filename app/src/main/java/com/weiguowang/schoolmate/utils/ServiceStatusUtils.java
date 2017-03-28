package com.weiguowang.schoolmate.utils;

import java.util.List;

import android.app.ActivityManager;
import android.app.ActivityManager.RunningServiceInfo;
import android.content.Context;

public class ServiceStatusUtils {
	
	
	/**
	 * У��ĳ�������Ƿ����
	 * @param context ������
	 * @param serviceName ��������
	 * @return
	 */
	
	public static boolean isRunningService(Context context ,String serviceName){
		//����Activity �����Թ������
		ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
		List<RunningServiceInfo> serviceInfos = am.getRunningServices(100);
		for(RunningServiceInfo serviceInfo : serviceInfos){
			String name = serviceInfo.service.getClassName();
			if(serviceName.equals(name)){
				return true;
			}
		}
		return false;
	}

}
