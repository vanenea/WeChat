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
public class Config {
	
	public static String className;
	public static String url;
	public static String username;
	public static String password;
	public static String initSize;
	public static String maxActive;
	public static BasicDataSource bds;
	public static int port;
	static {
		bds = new BasicDataSource();
		Properties p = new Properties();
		InputStream inStream = Config.class.getClassLoader().getResourceAsStream("db.properties");
		try {
			p.load(inStream);
			
			//数据库设置
			bds.setDriverClassName(p.getProperty("driver"));
			bds.setUrl(p.getProperty("url"));
			bds.setUsername(p.getProperty("username"));
			bds.setPassword(p.getProperty("password"));
			bds.setInitialSize(Integer.parseInt(p.getProperty("initSize")));
			bds.setMaxTotal(Integer.parseInt(p.getProperty("maxActive")));
			
			//prot
			port = Integer.parseInt(p.getProperty("port"));
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
