package com.weiguowang.schoolmate.utils;

import java.util.Map;

import org.apache.commons.beanutils.BeanUtils;

public class MyBeanUtils {

	public static <T> T populate(Class<T> beanClass , Map<String,String[]> data){
		
		
		try {
			//如果有时间处理
			
			//创建javabean实例
			T bean = beanClass.newInstance();
			BeanUtils.populate(bean, data);
			return bean;
		} catch (Exception e) {
			throw new RuntimeException(e);
		}
	}
	
	
}
