package com.weiguowang.schoolmate.utils;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * function:
 * Created by 韦国旺 on 2017/4/5 0005.
 * Copyright (c) 2017 北京联龙博通 All Rights Reserved.
 */
public class DownloadService {
    private static final String TAG = "DownloadService";

    public String getImageURI(String path, File cache) {
        String name = MD5Util.ToMD5(path) + path.substring(path.lastIndexOf("."));
        File file = new File(cache, name);
        // ���ͼƬ���ڱ��ػ���Ŀ¼�������ļ��Ļ���ʱ�䲻����1�죬��ȥ����������

        if (file.exists()
                && System.currentTimeMillis() - file.lastModified() < 10 * 60 * 60 * 24) {
            LogUtil.info("ȡ�������Ӧ�ļ����޸�ʱ��:" + file.lastModified());
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
                    LogUtil.info("�ļ������޸ĵ�ʱ��:" + System.currentTimeMillis());
                    file.setLastModified(System.currentTimeMillis());
                    is.close();
                    fos.close();
                    return file.toString();
                }
            } catch (IOException e) {
                if (file.exists()) {
                    LogUtil.info("������ȡ�����ļ���:" + file.lastModified());
                    return file.toString();
                }
            }
        }
        return null;
    }
}
