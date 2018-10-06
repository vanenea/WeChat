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
import java.util.HashMap;
import java.util.Map;
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
	public static Map<String, User> users = new HashMap<String, User>();

	
	public void start() {
		LOGGER.info("启动服务器中...");
		try {
			serverSocket = new ServerSocket(Config.port);
			ExecutorService pool = Executors.newFixedThreadPool(10);
			LOGGER.info("服务器启动成功");
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
			if(serverSocket!=null) {
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
				short command = -1;
				while((command = IOUtils.readShort(in))!=-1) {
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
				users.remove(user.getUsername());
				reFreshList();
			}
		}

		private void doSendTextToOne() {
			String toUser = IOUtils.readString(in);
			String fromUser = IOUtils.readString(in);
			String message = IOUtils.readString(in);
			User toUsr = users.get(toUser);
			if(toUsr!=null) { 
				try {
					IOUtils.writeShort(toUsr.getSocket().getOutputStream(), ResponseCommand.MESSAGE_TO_ONE_RESPONSE);
					IOUtils.writeString(toUsr.getSocket().getOutputStream(), fromUser);
					IOUtils.writeString(toUsr.getSocket().getOutputStream(), message);
				} catch (IOException e) {
					LOGGER.error("发送私聊失败", e);
				}
			}
		}

		private void doSendText() {
			String fromUser = IOUtils.readString(in);
			String message = IOUtils.readString(in);
			
			for(User usr : users.values()) {
				if(user.getUsername().equals(usr.getUsername())) {
					continue;
				}
				try {
					IOUtils.writeShort(usr.getSocket().getOutputStream(), ResponseCommand.MESSAGE_TO_ALL_RESPONSE);
					IOUtils.writeString(usr.getSocket().getOutputStream(), fromUser);
					IOUtils.writeString(usr.getSocket().getOutputStream(), message);
				} catch (IOException e) {
					LOGGER.error("发送群聊消息失败", e);
				}
			}
			
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
			String toUser = IOUtils.readString(in);
			String fromUser = IOUtils.readString(in);
			String fileName = IOUtils.readString(in);
			//文件长度
			long length = IOUtils.readLong(in);
			User user = users.get(toUser);
			if(user != null) {
				try {
					IOUtils.writeShort(user.getSocket().getOutputStream(), ResponseCommand.FILE_TO_ONE_RESPONSE);
					IOUtils.writeString(user.getSocket().getOutputStream(), fromUser);
					IOUtils.writeString(user.getSocket().getOutputStream(), fileName);
					IOUtils.writeLong(user.getSocket().getOutputStream(), length);
					//读取的总长度
					long sum = 0;
					byte[] buf = new byte[(int)(1024>length?length:1024)];
					while(true) {
						in.read(buf);
						user.getSocket().getOutputStream().write(buf);
						sum += buf.length; 
						if(sum >= length) {
							break;
						}
						buf = new byte[(int)(1024>(length-sum)?(length-sum):1024)];
					}
				
				} catch (IOException e) {
					LOGGER.error("文件传输失败", e);
				}
			}
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
						IOUtils.writeString(out, "loginSuccess");
						user = new User();
						user.setId(id);
						user.setUsername(userName);
						user.setPassword(password);
						user.setSocket(socket);
						users.put(userName, user);
						reFreshList();
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

		private void reFreshList() {
			StringBuilder sb = new StringBuilder();
			sb.append("自己,");
			for(String name : users.keySet()) {
				sb.append(name+",");
			}
			for(User usr : users.values()) {
				try {
					String str = sb.toString().replace(usr.getUsername()+",", "");
					IOUtils.writeShort(usr.getSocket().getOutputStream(), ResponseCommand.USER_LIST_RESPONSE);
					IOUtils.writeString(usr.getSocket().getOutputStream(), str);
				} catch (IOException e) {
					LOGGER.error("获取用户列表失败", e);
				}
			}
		}
		
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
}
