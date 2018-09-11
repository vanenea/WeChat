package com.chen.utils;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.SQLException;
import java.util.Properties;

import org.apache.commons.dbcp2.BasicDataSource;

/**
 * 数据连接工具类
 * @author Administrator
 *
 */
public class DBUtil {
	
	static String className;
	static String url;
	static String username;
	static String password;
	static String initSize;
	static String maxActive;
	static BasicDataSource bds;
	
	static {
		bds = new BasicDataSource();
		Properties p = new Properties();
		InputStream inStream = DBUtil.class.getClassLoader().getResourceAsStream("db.properties");
		try {
			p.load(inStream);
			bds.setDriverClassName(p.getProperty("driver"));
			bds.setUrl(p.getProperty("url"));
			bds.setUsername(p.getProperty("username"));
			bds.setPassword(p.getProperty("password"));
			bds.setInitialSize(Integer.parseInt(p.getProperty("initSize")));
			bds.setMaxTotal(Integer.parseInt(p.getProperty("maxActive")));
			
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	/**
	 * 获取连接
	 * @return
	 * @throws SQLException
	 */
	public static Connection getConnection() throws SQLException {
		return bds.getConnection();
	}
	
	/**
	 * 关闭连接
	 * @param conn
	 */
	public void closeConn(Connection conn) {
		if(conn!=null) {
			try {
				conn.close();
			} catch (SQLException e) {
				e.printStackTrace();
			}
		}
	}
}
