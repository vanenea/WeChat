package com.chen.servers;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chen.model.User;
import com.chen.utils.Config;
import com.chen.utils.IOUtils;
import com.chen.utils.RequestCommand;
import com.chen.utils.ResponseCommand;

/**
 * 服务端
 * @author Administrator
 *
 */
public class Server {
	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
	private static long count = 0;
	private ServerSocket serverSocket;
	public static List<User> users = new ArrayList<User>();
	
	public void start() {
		LOGGER.info("启动服务器");
		try {
			serverSocket = new ServerSocket(Config.port);
			ExecutorService pool = Executors.newFixedThreadPool(10);
			while(true) {
				Socket socket = serverSocket.accept();
				pool.execute(new HandServer(socket));
				LOGGER.info("客户端连接进来");
				count ++;
				LOGGER.info("当前在线人数:"+count);
				}
		} catch (IOException e) {
			LOGGER.error("启动服务器失败",e);
		} finally {
			count --;
			LOGGER.info("当前在线人数:"+count);
			if(!serverSocket.isClosed()) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					LOGGER.error("关闭服务器异常",e);
				}
			}
		}
	}
	
	/**
	 * 	处理服务器类
	 * @author chen
	 *
	 */
	class HandServer implements Runnable {
		//套接字
		private Socket socket;
		//输入流
		private InputStream in;
		//输出流
		private OutputStream out;
		
		private User user;
		
		public HandServer(Socket socket) {
			this.socket = socket;
		}
		
		public void run() {
			try {
				in = socket.getInputStream();
				out = socket.getOutputStream();
				int command = -1;
				while((command = in.read())!=-1) {
					switch (command) {
					//登陆操作
					case RequestCommand.LOGIN:
						LOGGER.info("登陆操作");
						doLogin();
						break;
					//注册操作
					case RequestCommand.REG:
						LOGGER.info("注册操作");
						doReg();
						break;
					//广播文件
					case RequestCommand.SEND_FILE:
						LOGGER.info("广播文件");
						doSendFile();
						break;
					//请求个人传送文件
					case RequestCommand.SEND_FILE_TO_ONE:
						LOGGER.info("请求个人传送文件");
						doSendFileToOne();
						break;
					//请求回复发件人结果
					case RequestCommand.SEND_FILE_TO_ONE_1:
						LOGGER.info("请求回复发件人结果");
						doSendFileToOne1();
						break;
					//请求开始传送文件
					case RequestCommand.SEND_FILE_TO_ONE_2:
						LOGGER.info("请求开始传送文件");
						doSendFileToOne2();
						break;
					//广播文本信息
					case RequestCommand.SEND_TEXT:
						LOGGER.info("广播文本信息");
						doSendText();
						break;
					//文本私聊
					case RequestCommand.SEND_TEXT_TO_ONE:
						LOGGER.info("文本私聊");
						doSendTextToOne();
						break;
					default:
						break;
					}
					
				}
			} catch (Exception e) {
				LOGGER.error("", e);
			} finally {
				Server.users.remove(user);
			}
		}

		private void doSendTextToOne() {
			// TODO Auto-generated method stub
			
		}

		private void doSendText() {
			// TODO Auto-generated method stub
			
		}

		private void doSendFileToOne2() {
			// TODO Auto-generated method stub
			
		}

		private void doSendFileToOne1() {
			// TODO Auto-generated method stub
			
		}

		private void doSendFileToOne() {
			// TODO Auto-generated method stub
			
		}

		private void doSendFile() {
			// TODO Auto-generated method stub
			
		}

		private void doReg() {
			// TODO Auto-generated method stub
			
		}

		private void doLogin() {
			String userName = IOUtils.readString(in);
			String password = IOUtils.readString(in);
			try {
				Connection con = Config.getConnection();
				String sql = "select id,username,password from user where userName=?";
				PreparedStatement ps = con.prepareStatement(sql);
				ps.setString(1, userName);
				ResultSet rs = ps.executeQuery();
				if(rs.next()) {
					Integer id = rs.getInt(1);
					String word = rs.getString(3);
					if(word.equals(password)) {
						LOGGER.info("登录成功");
						IOUtils.writeShort(out, ResponseCommand.LOGIN_RESPONSE);
						IOUtils.writeString(out, "登录成功");
						user = new User();
						user.setId(id);
						user.setUsername(userName);
						user.setPassword(password);
						Server.users.add(user);
						
					} else {
						LOGGER.info("密码错误");
						IOUtils.writeShort(out, ResponseCommand.LOGIN_RESPONSE);
						IOUtils.writeString(out, "密码错误");
					}
				} else {
					LOGGER.error("账户不存在");
					IOUtils.writeShort(out, ResponseCommand.LOGIN_RESPONSE);
					IOUtils.writeString(out, "用户名不存在");
				}
			} catch (SQLException e) {
				LOGGER.error("链接数据库异常", e);
				IOUtils.writeShort(out, ResponseCommand.LOGIN_RESPONSE);
				IOUtils.writeString(out, "服务器异常，请稍后重试");
			}
		}
		
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
}
