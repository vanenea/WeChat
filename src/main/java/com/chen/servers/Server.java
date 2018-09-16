package com.chen.servers;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.chen.utils.Config;

/**
 * 服务端
 * @author Administrator
 *
 */
public class Server {
	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
	private static long count = 0;
	private ServerSocket serverSocket;
	
	public void start() {
		LOGGER.info("启动服务器");
		try {
			serverSocket = new ServerSocket(Config.port);
			ExecutorService pool = Executors.newFixedThreadPool(10);
			while(true) {
				Socket socket = serverSocket.accept();
				LOGGER.info("客户端连接进来");
				count ++;
				LOGGER.info("当前在线人数:"+count);
				}
		} catch (IOException e) {
			LOGGER.error("启动服务器失败",e);
		} finally {
			if(!serverSocket.isClosed()) {
				try {
					serverSocket.close();
				} catch (IOException e) {
					LOGGER.error("关闭服务器异常",e);
				}
			}
		}
	}
	
	public void handServer() {
		
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
}
