package com.weiguowang.schoolmate.utils;

import java.sql.Connection;
import java.sql.SQLException;

import javax.sql.DataSource;

import com.mchange.v2.c3p0.ComboPooledDataSource;

public class JdbcUtils {

	//创建数据源
	private static DataSource dataSource = new ComboPooledDataSource("ebook_config");
	
	
	/**
	 * 获得数据源
	 * @author lt
	 * @return
	 */
	public static DataSource getDataSource(){
		return dataSource;
	}
	
	/**
	 * 获得连接 -- 事务
	 * @author lt
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException{
		return getDataSource().getConnection();
	}
	
}
