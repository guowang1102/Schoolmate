package com.weiguowang.schoolmate.utils;

import android.app.ActivityManager;
import android.content.Context;

import java.util.List;

/**
 * function:
 * Created by 韦国旺 on 2017/4/5 0005.
 * Copyright (c) 2017 北京联龙博通 All Rights Reserved.
 */
public class ServiceStatusUtils {
    public static boolean isRunningService(Context context , String serviceName){
        //����Activity �����Թ������
        ActivityManager am = (ActivityManager) context.getSystemService(Context.ACTIVITY_SERVICE);
        List<ActivityManager.RunningServiceInfo> serviceInfos = am.getRunningServices(100);
        for(ActivityManager.RunningServiceInfo serviceInfo : serviceInfos){
            String name = serviceInfo.service.getClassName();
            if(serviceName.equals(name)){
                return true;
            }
        }
        return false;
    }
}
