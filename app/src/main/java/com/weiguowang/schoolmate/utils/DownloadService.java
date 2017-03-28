package com.weiguowang.schoolmate.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

import com.dfzt.wifui.utils.Logger;
import com.dfzt.wifui.utils.MD5;

/**
 * ��������ͼƬ
 * 
 * @author xia
 * 
 */
public class DownloadService {

	private static final String TAG = "DownloadService";

	public String getImageURI(String path, File cache) {
		String name = MD5.getMD5(path) + path.substring(path.lastIndexOf("."));
		File file = new File(cache, name);
		// ���ͼƬ���ڱ��ػ���Ŀ¼�������ļ��Ļ���ʱ�䲻����1�죬��ȥ����������

		if (file.exists()
				&& System.currentTimeMillis() - file.lastModified() < 10 * 60 * 60 * 24) {
			Logger.i(TAG, "ȡ�������Ӧ�ļ����޸�ʱ��:" + file.lastModified());
			return file.toString();// Uri.fromFile(path)��������ܵõ��ļ���URI
		} else {
			try {
				// �������ϻ�ȡͼƬ
				URL url = new URL(path);
				HttpURLConnection conn = (HttpURLConnection) url.openConnection();
				conn.setConnectTimeout(5000);
				conn.setRequestMethod("GET");
				conn.setDoInput(true);
				if (conn.getResponseCode() == 200) {
					InputStream is = conn.getInputStream();
					FileOutputStream fos = new FileOutputStream(file);
					byte[] buffer = new byte[1024];
					int len = 0;
					while ((len = is.read(buffer)) != -1) {
						fos.write(buffer, 0, len);
					}
					// �����ļ�����޸ĵ�ʱ�䣬�´��ٷ���ʱ����ʱ���ж��Ƿ�����������
					Logger.i(TAG, "�ļ������޸ĵ�ʱ��:" + System.currentTimeMillis());
					file.setLastModified(System.currentTimeMillis());
					is.close();
					fos.close();
					return file.toString();
				}
			} catch (IOException e) {
				if (file.exists()) {
					Logger.i(TAG, "������ȡ�����ļ���:" + file.lastModified());
					return file.toString();
				}
			}
		}
		return null;
	}
}
