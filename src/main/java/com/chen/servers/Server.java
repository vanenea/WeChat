package com.chen.servers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * 服务端
 * @author Administrator
 *
 */
public class Server {
	private static final Logger LOGGER = LoggerFactory.getLogger(Server.class);
	
	public void start() {
		LOGGER.info("启动服务器");
	}
	
	public static void main(String[] args) {
		Server server = new Server();
		server.start();
	}
}
