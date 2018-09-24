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
	public static int initSize;
	public static int maxActive;
	public static BasicDataSource bds;
	public static int port;
	static {
		bds = new BasicDataSource();
		Properties p = new Properties();
		InputStream inStream = Config.class.getClassLoader().getResourceAsStream("config.properties");
		try {
			p.load(inStream);
			className = p.getProperty("driver");
			url = p.getProperty("url");
			username = p.getProperty("username");
			password = p.getProperty("password");
			initSize = Integer.parseInt(p.getProperty("initSize"));
			maxActive = Integer.parseInt(p.getProperty("maxActive"));
			//数据库设置
			bds.setDriverClassName(className);
			bds.setUrl(url);
			bds.setUsername(username);
			bds.setPassword(password);
			bds.setInitialSize(initSize);
			bds.setMaxTotal(maxActive);
			
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
