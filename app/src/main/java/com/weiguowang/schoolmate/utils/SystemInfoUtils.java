package com.weiguowang.schoolmate.utils;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStreamReader;

import android.app.ActivityManager;
import android.app.ActivityManager.MemoryInfo;
import android.content.Context;

public class SystemInfoUtils {

	/**
	 * �õ���ǰ�ֻ������еĽ�������
	 */

	public static int getProcessCount(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		return am.getRunningAppProcesses().size();
	}

	/**
	 * �õ������ڴ�ram
	 */

	public static long getAvailRam(Context context) {
		ActivityManager am = (ActivityManager) context
				.getSystemService(Context.ACTIVITY_SERVICE);
		MemoryInfo outInfo = new MemoryInfo();
		am.getMemoryInfo(outInfo);
		return outInfo.availMem;

	}

	/**
	 * �õ����ڴ�ram
	 */

	public static long getTotalRam(Context context) {
		// ����Ĵ��룬��4.0�Ժ��֧��
		// ActivityManager am = (ActivityManager)
		// context.getSystemService(Context.ACTIVITY_SERVICE);
		// MemoryInfo outInfo = new MemoryInfo();
		// am.getMemoryInfo(outInfo);
		// return outInfo.totalMem;
		// MemTotal: 516452 kB
		File file = new File("/proc/meminfo");
		try {
			FileInputStream fis = new FileInputStream(file);
			BufferedReader reader = new BufferedReader(new InputStreamReader(
					fis));
			String line = reader.readLine();
			StringBuffer buffer = new StringBuffer();
			for (char c : line.toCharArray()) {
				if (c >= '0' && c <= '9') {
					buffer.append(c);
				}
			}
			fis.close();
			reader.close();
			return Integer.valueOf(buffer.toString()) * 1024;

			// �ַ���--�ַ�

		} catch (Exception e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return 0;
		}

	}

}
