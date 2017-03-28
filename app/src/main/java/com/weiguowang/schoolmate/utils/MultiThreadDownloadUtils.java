package com.weiguowang.schoolmate.utils;

import android.os.Handler;
import android.widget.LinearLayout;
import android.widget.ProgressBar;

import java.io.File;
import java.io.InputStream;
import java.io.RandomAccessFile;
import java.net.HttpURLConnection;
import java.net.URL;

public class MultiThreadDownloadUtils {

    private int threadCount;        // �̵߳�����
    private int runCompleteThreadCount;    // ��������̵߳ļ�����
    private LinearLayout llGroup;
    private Handler handler;

    /**
     * ��ʼ���߳�����
     *
     * @param url         ��������
     * @param threadCount �̵߳�����
     * @param directory   �洢��·��
     * @param handler
     */
    public void startMultiThreadDownload(String url, int threadCount,
                                         String directory, LinearLayout llGroup, Handler handler) throws Exception {
        this.handler = handler;
        this.threadCount = threadCount;
        this.llGroup = llGroup;
        runCompleteThreadCount = 0;

        //1. �õ��������ļ��ĳ���.
        int remoteFileLength = getFileLengthFromNet(url);

        if (remoteFileLength == -1) {
            throw new IllegalArgumentException("��ȡԶ���ļ��ĳ���ʧ��.");
        }

        System.out.println("Զ���ļ��ĳ���: " + remoteFileLength);

        //2. �����ļ��ĳ��ȴ���һ�����ص��ļ�.
        String fileName = url.substring(url.lastIndexOf("/") + 1);
        File file = new File(directory, fileName);        // /mnt/sdcard/FeiQ.exe
        RandomAccessFile raf = new RandomAccessFile(file, "rwd");
        // ����ΪԶ�̷������ļ��ĳɶ�
        raf.setLength(remoteFileLength);
        raf.close();

        //3. �����ÿһ���߳���Ҫ���صĴ�С.
        int partSize = remoteFileLength / threadCount;        // ÿһ��Ĵ�С

        // ����һ����Ϣ�����߳�(MainActivity)��, ������ʼ���ص���˾
//		handler.sendEmptyMessage(MainActivity.START_DONWLOAD);

        for (int i = 0; i < threadCount; i++) {
            // ��ʼ��λ��: �߳�id * ��Ĵ�С;
            int start = i * partSize;
            // ������λ��: ((�߳�id + 1) * ��Ĵ�С) -1;
            int end = ((i + 1) * partSize) - 1;

            // ��������һ���߳�, ��Ҫ���ص����. �ļ����� -1;
            if (i == threadCount - 1) {
                end = remoteFileLength - 1;
            }

            //4. �����߳�����.
            // �������߳�ʱ, ��Ҫ�������̵߳�������: �߳�id, ��ʼ��λ��, ������λ��, ���ص����ӵ�ַ, �ļ��洢��·��(�����ļ���).
            new DownloadThread(i, start, end, url, file.getPath()).start();
        }
    }

    /**
     * ����url���ӻ�ȡ��Ҫ�����ļ��ĳ���
     *
     * @param url
     * @return -1 ��ȡʧ��
     * @throws Exception
     */
    private int getFileLengthFromNet(String url) throws Exception {
        HttpURLConnection conn = (HttpURLConnection) new URL(url).openConnection();
        conn.setRequestMethod("GET");
        conn.setConnectTimeout(10000);
        conn.setReadTimeout(10000);

        int responseCode = conn.getResponseCode();
        if (responseCode == 200) {
            // ��ȡ�ļ��ĳ���, ����.
            int length = conn.getContentLength();// ��ȡԶ���ļ��ĳ���
            conn.disconnect();
            return length;
        }
        conn.disconnect();
        return -1;
    }

    /**
     * @author andong
     *         ���߳������е����߳�ҵ����
     */
    class DownloadThread extends Thread {

        private int threadID;
        private int start;
        private int end;
        private String url;
        private String path;

        /**
         * @param i     �߳�id
         * @param start ��ʼ��λ��
         * @param end   ������λ��
         * @param url   ��������
         * @param path  �ļ���·����(�����ļ���)
         */
        public DownloadThread(int i, int start, int end, String url, String path) {
            this.threadID = i;
            this.start = start;
            this.end = end;
            this.url = url;
            this.path = path;
        }

        @Override
        public void run() {
            // ���õ�ǰ�������ֵ
            ProgressBar pb = (ProgressBar) llGroup.getChildAt(threadID);    // ��ȡ��ǰ�̶߳�Ӧ�Ľ�����
            pb.setMax(end - start);

            HttpURLConnection conn = null;
            RandomAccessFile cacheFileRAF;

            // ���建���Ŀ¼: /mnt/sdcard/FeiQ.exe		/mnt/sdcard/�߳�1.txt
            String cacheDirectory = path.substring(0, path.lastIndexOf("/"));
            try {
                File cacheFile = new File(cacheDirectory, "�߳�" + threadID + ".txt");
                if (cacheFile.exists()) {        // ��ǰ�ļ�����, ˵���ϴ��Ƕϵ�����
                    // �ѱ����ļ��Ľ���ȡ����
                    cacheFileRAF = new RandomAccessFile(cacheFile, "rwd");
                    // �ϴ����صĽ���
                    int previousProgress = Integer.valueOf(cacheFileRAF.readLine());
                    cacheFileRAF.close();

                    // �ѵ�ǰ�Ľ���ֵ, ����Ϊ��һ�����صĽ���
                    pb.setProgress(previousProgress);

                    start += previousProgress;
                }

                // 4. ��ʼ����
                System.out.println("�߳�" + threadID + ", ��ʼ����: " + start + "-" + end);
                conn = (HttpURLConnection) new URL(url).openConnection();
                conn.setRequestMethod("GET");
                conn.setConnectTimeout(10000);
                conn.setReadTimeout(10000);
                // 4.1. �ڻ�ȡ״̬��֮ǰ, ��������ͷRange, ������������ļ��Ĳ�������.
                conn.setRequestProperty("Range", "bytes=" + start + "-" + end);    // �����������3-5λ�õ�����.

                // 4.2. ��Ϊ������ǲ�������, ��ʱ����������ص�״̬��: 206
                int responseCode = conn.getResponseCode();
                if (responseCode == 206) {    // ��ǰ������֧�����󲿷�����, �����Ѿ��Ѳ������ݷ��ع�����.
                    // 4.3. ��д�뱾���ļ�ʱ, ��Ҫ�ѱ����ļ���ʼд�������, �ƶ�����ǰ�߳̿�ʼ��λ��.
                    RandomAccessFile raf = new RandomAccessFile(path, "rwd");
                    raf.seek(start);        // �ѵ�ǰ�ļ��������ƶ�����ʼ��λ����

                    // 4.4. ȡ����������. ѭ����ʼ��ȡ, ��д�뵽����.
                    InputStream is = conn.getInputStream(); // ��������
                    byte[] buffer = new byte[102400];
                    int len = -1;
                    int downloadTotalLength = 0;        // ��ǰ�߳��������ܳ���

                    while ((len = is.read(buffer)) != -1) {        // ��ʼ����, ����������
                        raf.write(buffer, 0, len);

                        // ͻȻ, �ֻ��ϵ���.  �ѵ�ǰ�������صĽ���, �洢������
                        downloadTotalLength += len;
                        cacheFileRAF = new RandomAccessFile(cacheFile, "rwd");
                        cacheFileRAF.write(String.valueOf(downloadTotalLength).getBytes());
                        cacheFileRAF.close();

                        // ÿ�ζ�ȡһ������ʱ, ����һ�½���ֵ.
                        pb.incrementProgressBy(len);
                    }
                    raf.close();
                    is.close();

                    System.out.println("�߳�" + threadID + ", �������.");
                    runCompleteThreadCount++;        // ÿ���߳����һ��, ������+1
                }
            } catch (Exception e) {
//				handler.sendEmptyMessage(MainActivity.FAILED);
                e.printStackTrace();
            } finally {
                if (conn != null) {
                    conn.disconnect();
                }

                if (runCompleteThreadCount == threadCount) {
                    System.out.println("�����̶߳��������.");
//					handler.sendEmptyMessage(MainActivity.SUCCESS);
                    // ɾ�����е������ļ�
                    for (int i = 0; i < threadCount; i++) {
                        File file = new File(cacheDirectory, "�߳�" + i + ".txt");
                        if (file.exists()) {        // �������ɾ���ļ�.
                            System.out.println(file.delete());
                        }
                    }
                }
            }
        }
    }
}
