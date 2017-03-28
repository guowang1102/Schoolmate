package com.weiguowang.schoolmate.utils;

import java.io.File;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.util.Arrays;
import java.util.UUID;

public class MyStringUtils {
	
	/**
	 * 获得32长度UUID值
	 * @author lt
	 * @return
	 */
	public static String getUUID(){
		return UUID.randomUUID().toString().replace("-", "");
	}
	
	
	/**
	 * 将给定的数据进行MD5加密
	 * @author lt
	 * @param data
	 * @return
	 */
	public static String getMD5Value(String data){
		try {
			//1 获得加密算法
			MessageDigest messageDigest = MessageDigest.getInstance("MD5");
			//2 加密
			byte[] md5ByteArray = messageDigest.digest(data.getBytes());
			
			//3 将10进制 转换成16进制
			return new BigInteger(1,md5ByteArray).toString(16);
			
		} catch (Exception e) {
			return data;
		}
	}
	
	/**
	 * 获得指定文件的二级目录
	 * @author lt
	 * @param filename
	 * @return
	 */
	public static String getDir(String filename){
		if(filename == null){
			return null;
		}
		// 1 获得数据
		int code = filename.hashCode();
		//System.out.println(code);
		// 2 第一层目录
		int d1 = code & 0xf;
		// 3 第二层目录
		int d2 = (code >>> 4) & 0xf;
//		return File.separator + d1 + File.separator + d2;
		return "/" + d1 + "/" + d2;
	}
	
	public static void main(String[] args) {
		System.out.println(getMD5Value("1234"));
		System.out.println(getMD5Value("1234").length());
	}

}
