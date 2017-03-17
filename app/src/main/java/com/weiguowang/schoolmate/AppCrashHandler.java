package com.weiguowang.schoolmate;

import android.content.Context;
import android.os.Build;
import android.os.Environment;


import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.LineNumberReader;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.io.Writer;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * function : 异常日志记录
 * Created by 韦国旺 on 2016/6/15.
 * Copyright (c) 2016 北京联龙博通 All Rights Reserved.
 */
public class AppCrashHandler {

    private Context context;
    private static AppCrashHandler instance;
    private Thread.UncaughtExceptionHandler uncaughtExceptionHandler;

    public static AppCrashHandler getInstance(Context context) {
        if (instance == null) {
            instance = new AppCrashHandler(context);
        }
        return instance;
    }

    private AppCrashHandler(Context context) {
        this.context = context;
        uncaughtExceptionHandler = Thread.getDefaultUncaughtExceptionHandler();
        Thread.setDefaultUncaughtExceptionHandler(new Thread.UncaughtExceptionHandler() {
            @Override
            public void uncaughtException(Thread thread, Throwable throwable) {
                saveException(throwable);
                uncaughtExceptionHandler.uncaughtException(thread, throwable);
            }
        });
    }

    public static boolean isStorageReady(){
        return Environment.getExternalStorageState().equals(Environment.MEDIA_MOUNTED);
    }

    public static final String CARSH_PATH = Environment.getExternalStorageDirectory().getAbsolutePath()
            + File.separator + "Carsh" + File.separator;

    /**
     * 保存日志文件
     *
     * @param ex
     */
    public void saveException(Throwable ex) {

        if (!isStorageReady()) {
            return;
        }

        Writer writer = null;
        PrintWriter printWriter = null;
        String stackTrace = "";
        try {
            writer = new StringWriter();
            printWriter = new PrintWriter(writer);
            ex.printStackTrace(printWriter);
            Throwable cause = ex.getCause();
            while (cause != null) {
                cause.printStackTrace(printWriter);
                cause = cause.getCause();
            }
            stackTrace = writer.toString();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (writer != null) {
                try {
                    writer.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (printWriter != null) {
                printWriter.close();
            }
        }

        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
        Date date = new Date();
        String timestamp = sdf.format(date);

        String filename = CARSH_PATH + File.separator + formatTimeString(System.currentTimeMillis()) + ".txt";
        BufferedWriter mBufferedWriter = null;
        try {
            File mFile = new File(filename);
            File pFile = mFile.getParentFile();
            if (!pFile.exists()) {// 如果文件夹不存在，则先创建文件夹
                pFile.mkdirs();
            }
            int count = 1;
            if (mFile.exists()) {
                LineNumberReader reader = null;
                try {
                    reader = new LineNumberReader(new FileReader(mFile));
                    String line = reader.readLine();
                    if (line.startsWith("count")) {
                        int index = line.indexOf(":");
                        if (index != -1) {
                            String count_str = line.substring(++index);
                            if (count_str != null) {
                                count_str = count_str.trim();
                                count = Integer.parseInt(count_str);
                                count++;
                            }
                        }
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                } finally {
                    if (reader != null) {
                        try {
                            reader.close();
                        } catch (Exception e) {
                            e.printStackTrace();
                        }

                    }
                }
                mFile.delete();
            }
            mFile.createNewFile();
            mBufferedWriter = new BufferedWriter(new FileWriter(mFile, true));// 追加模式写文件
            mBufferedWriter.append(snapshot(timestamp, stackTrace, count));
            mBufferedWriter.flush();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            if (mBufferedWriter != null) {
                try {
                    mBufferedWriter.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    public static String formatTimeString(long when) {
        String formatStr = "yyyy-MM-dd HH:MM";
        SimpleDateFormat sdf = new SimpleDateFormat(formatStr);
        return sdf.format(when);
    }

    public static String snapshot(String timestamp, String trace, int count) {
        Map<String, String> info = new LinkedHashMap<String, String>();
        info.put("count: ", String.valueOf(count));
        info.put("time: ", timestamp);
        info.put("device: ", Build.MODEL);
        info.put("os: ", Build.VERSION.SDK_INT + "");
        Iterator<Map.Entry<String, String>> iterator = info.entrySet().iterator();
        StringBuilder sb = new StringBuilder();
        while (iterator.hasNext()) {
            Map.Entry<String, String> entry = iterator.next();
            if (entry != null) {
                sb.append(entry.getKey()).append(entry.getValue());
                sb.append(System.getProperty("line.separator"));
            }
        }
        sb.append(System.getProperty("line.separator"));
        sb.append(trace);
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));
        sb.append(System.getProperty("line.separator"));
        return sb.toString();
    }

}
